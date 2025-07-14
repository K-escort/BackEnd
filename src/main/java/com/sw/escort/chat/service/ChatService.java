package com.sw.escort.chat.service;

import com.sw.escort.chat.dto.req.ChatStartReq;
import com.sw.escort.chat.dto.res.ChatResponse;

public interface ChatService {
    ChatResponse.ChatDetail startChat(Long userId, ChatStartReq req);
}

