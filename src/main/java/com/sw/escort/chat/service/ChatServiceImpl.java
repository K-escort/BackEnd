package com.sw.escort.chat.service;

import com.sw.escort.apiPayload.code.exception.GeneralException;
import com.sw.escort.apiPayload.code.status.ErrorStatus;
import com.sw.escort.chat.dto.req.ChatStartReq;
import com.sw.escort.chat.dto.res.ChatResponse;
import com.sw.escort.common.client.PythonAiClient;
import com.sw.escort.user.entity.User;
import com.sw.escort.user.entity.UserInfo;
import com.sw.escort.user.repository.UserInfoRepository;
import com.sw.escort.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final PythonAiClient pythonAiClient;

    @Override
    public ChatResponse.ChatDetail startChat(Long userId, ChatStartReq req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        UserInfo info = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_INFO_NOT_FOUND));

        return pythonAiClient.sendChatToPython(user, info, req);
    }
}

