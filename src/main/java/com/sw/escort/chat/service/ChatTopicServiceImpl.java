package com.sw.escort.chat.service;

import com.sw.escort.apiPayload.code.exception.GeneralException;
import com.sw.escort.apiPayload.code.status.ErrorStatus;
import com.sw.escort.chat.dto.res.ChatResponse;
import com.sw.escort.common.client.PythonAiClient;
import com.sw.escort.global.util.AmazonS3Util;
import com.sw.escort.user.entity.User;
import com.sw.escort.user.entity.UserInfo;
import com.sw.escort.user.repository.UserInfoRepository;
import com.sw.escort.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatTopicServiceImpl implements ChatTopicService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final PythonAiClient pythonAiClient;
    private final AmazonS3Util amazonS3Util;

    @Override
    public List<ChatResponse.TopicResponseDto> getTopicsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        UserInfo userInfo = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_INFO_NOT_FOUND));

        // AI 서버에 파일명 요청
        List<String> fileNames = pythonAiClient.fetchTopicFileNames(user.getId(), userInfo);

        // S3에서 URL 매핑
        return fileNames.stream()
                .map(fileName -> {
                    String url = amazonS3Util.getPublicDataImageUrlIfExists(fileName)
                            .orElseThrow(() -> new GeneralException(ErrorStatus.IMAGE_NOT_FOUND));
                    return new ChatResponse.TopicResponseDto(fileName, url);
                })
                .toList();
    }
}

