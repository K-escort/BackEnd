package com.sw.escort.daily.service;

import com.sw.escort.apiPayload.code.exception.GeneralException;
import com.sw.escort.apiPayload.code.status.ErrorStatus;
import com.sw.escort.common.client.PythonAiClient;
import com.sw.escort.daily.dto.res.DailyDtoRes;
import com.sw.escort.daily.entity.Daily;
import com.sw.escort.daily.entity.DailyConversation;
import com.sw.escort.daily.repository.DailyConversationRepository;
import com.sw.escort.daily.repository.DailyRepository;
import com.sw.escort.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DailyConversationServiceImpl implements DailyConversationService {
    private final PythonAiClient aiClient;
    private final DailyConversationRepository dailyConversationRepository;
    private final DailyRepository dailyRepository;
    private final UserRepository userRepository;

    @Override
    public DailyDtoRes.DailyConversationRes saveConversations(Long userId, LocalDate localDate) {


        Optional<Daily> dailyOptional = dailyRepository.findByUserIdAndDailyDayRecording(userId, localDate);

        Daily daily;
        if (dailyOptional.isPresent()) {
            daily = dailyOptional.get();
        } else {
            // 새로운 Daily 생성 및 필수 필드 설정
            daily = Daily.builder()
                    .user(userRepository.findById(userId)
                            .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND)))
                    .dailyDayRecording(localDate)
                    .build();
            dailyRepository.save(daily);
        }

        List<DailyConversation> existingConversations = dailyConversationRepository.findByDailyId(daily.getId());
        DailyDtoRes.ConversationRes conversationRes = aiClient.fetchAiConversation(userId, localDate);

        List<DailyDtoRes.EachConversationRes> conversations = conversationRes.getConversations();
        if (conversations != null && !conversations.isEmpty()) {
            for (DailyDtoRes.EachConversationRes eachConversation : conversations) {
                if(eachConversation.getSpeaker().equals("system")) continue;
                boolean isDuplicate = existingConversations.stream()
                        .anyMatch(ec -> ec.getSpeaker().equals(eachConversation.getSpeaker())
                                && ec.getContent().equals(eachConversation.getContent()));

                if (!isDuplicate) {
                    DailyConversation newConversation = DailyConversation.builder()
                            .daily(daily)
                            .speaker(eachConversation.getSpeaker())
                            .content(eachConversation.getContent())
                            .timeStamp(eachConversation.getTimeStamp())
                            .build();
                    dailyConversationRepository.save(newConversation);
                }
            }
             dailyConversationRepository.flush();
        }

        return DailyDtoRes.DailyConversationRes.builder()
                .dailyId(daily.getId())
                .build();
    }

}
