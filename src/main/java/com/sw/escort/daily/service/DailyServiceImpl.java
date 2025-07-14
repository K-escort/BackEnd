package com.sw.escort.daily.service;

import com.sw.escort.apiPayload.code.exception.GeneralException;
import com.sw.escort.apiPayload.code.status.ErrorStatus;
import com.sw.escort.common.client.PythonAiClient;
import com.sw.escort.daily.converter.DailyConverter;
import com.sw.escort.daily.dto.req.DailyDtoReq;
import com.sw.escort.daily.dto.res.DailyDtoRes;
import com.sw.escort.daily.entity.Daily;
import com.sw.escort.daily.entity.DailyConversation;
import com.sw.escort.daily.repository.DailyConversationRepository;
import com.sw.escort.daily.repository.DailyImageRepository;
import com.sw.escort.daily.repository.DailyRepository;
import com.sw.escort.global.util.AmazonS3Util;
import com.sw.escort.user.entity.User;
import com.sw.escort.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DailyServiceImpl implements DailyService {
    private final DailyRepository dailyRepository;
    private final UserRepository userRepository;
    private final AmazonS3Util amazonS3Util;
    private final DailyConverter dailyConverter;
    private final DailyConversationRepository dailyConversationRepository;
    private final DailyImageRepository dailyImageRepository;
    private static final int MAX_DAILY_IMAGES = 3; //그림 3개로 제한
    private final PythonAiClient pythonAiClient;

    @Override
    public void saveFeedback(Long userId, DailyDtoReq.RecordFeedbackReq req){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        if(!user.getRole().name().equals("HEALER")){
            throw new GeneralException(ErrorStatus.ONLY_HEALER);
        }
        dailyRepository.save(dailyConverter.toEntity(req, user));
    }

    @Override
    @Transactional(readOnly = true)
    public DailyDtoRes.DailyRes getDaily(Long userId, LocalDate date){
        Daily daily = dailyRepository.findByUserIdAndDailyDayRecording(userId, date)
                .orElseThrow(() -> new GeneralException(ErrorStatus.DAILY_NOT_FOUND));
        List<String> drawingImageUrls = amazonS3Util.getDailyImagePath(daily.getId());
        String dailyVideoUrl = amazonS3Util.getDailyVideoPath(daily.getId());
        List<DailyConversation> dailyConversations = dailyConversationRepository.findByDailyId(daily.getId());

        List<String> formattedConversations = dailyConversations.stream()
                .map(conv -> conv.getSpeaker() + ": " + conv.getContent())
                .collect(Collectors.toList());

        return DailyDtoRes.DailyRes.builder()
                .createdAt(daily.getCreatedAt())
                .updatedAt(daily.getUpdatedAt())
                .dailyDayRecording(daily.getDailyDayRecording())
                .imageUrls(drawingImageUrls)
                .videoUrls(dailyVideoUrl)
                .conversations(formattedConversations)
                .feedback(daily.getFeedback())
                .id(daily.getId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DailyDtoRes.MonthlyRes> getMonthly(Long userId, YearMonth ym) {
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth(); // 해당 달의 마지막 날

        List<Daily> monthlies = dailyRepository
                .findAllByUserIdAndDailyDayRecordingBetweenOrderByDailyDayRecordingAsc(userId, start, end);

        Map<LocalDate, Daily> dailyMap = monthlies.stream()
                .collect(Collectors.toMap(Daily::getDailyDayRecording, Function.identity()));

        List<DailyDtoRes.MonthlyRes> result = new ArrayList<>();
        int daysInMonth = ym.lengthOfMonth();

        for (int i = 0; i < daysInMonth; i++) {
            LocalDate current = start.plusDays(i);
            Daily daily = dailyMap.get(current);

            if (daily == null) {
                result.add(DailyDtoRes.MonthlyRes.builder()
                        .id(null)
                        .monthlyDayRecording(current)
                        .imageUrl(null)
                        .build());
            } else {
                List<String> imageUrls = amazonS3Util.getDailyImagePath(daily.getId());
                String imageUrl = (imageUrls != null && !imageUrls.isEmpty())
                        ? imageUrls.get(0)
                        : null;

                result.add(DailyDtoRes.MonthlyRes.builder()
                        .id(daily.getId())
                        .monthlyDayRecording(current)
                        .imageUrl(imageUrl)
                        .build());
            }
        }

        return result;
    }

}
