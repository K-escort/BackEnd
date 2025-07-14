package com.sw.escort.daily.service;

import com.sw.escort.apiPayload.code.exception.GeneralException;
import com.sw.escort.apiPayload.code.status.ErrorStatus;
import com.sw.escort.common.client.PythonAiClient;
import com.sw.escort.daily.converter.DailyConverter;
import com.sw.escort.daily.dto.req.DailyDtoReq;
import com.sw.escort.daily.entity.Daily;
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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DailyServiceImpl implements DailyService {
    private final DailyRepository dailyRepository;
    private final UserRepository userRepository;
    private final AmazonS3Util amazonS3Util;
    private final DailyConverter dailyConverter;
    private final DailyImageRepository dailyImageRepository;
    private static final int MAX_DAILY_IMAGES = 3; //그림 3개로 제한
    private final PythonAiClient pythonAiClient;

    @Override
    public void saveDaily(Long userId, DailyDtoReq.RecordDailyReq req, List<MultipartFile> dailyImages, List<MultipartFile> dailyVideos) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        if (dailyRepository.existsByUserIdAndDailyDayRecording(userId, req.getDailyDayRecording())) {
            throw new GeneralException(ErrorStatus.DAILY_ALREADY_EXISTS);
        }


        if(dailyImages != null){
            if(dailyImages.size() > MAX_DAILY_IMAGES){
                throw new GeneralException(ErrorStatus.FILE_ONLY_THREE);
            }
        }

        Daily savedDaily = dailyRepository.save(dailyConverter.toEntity(req, user));
        if (dailyImages != null && !dailyImages.isEmpty()) {
            amazonS3Util.uploadDailyImages(dailyImages, savedDaily);
        }

        if (dailyVideos != null && !dailyVideos.isEmpty()) {
            amazonS3Util.uploadDailyVideos(dailyVideos, savedDaily);
        }
    }
}
