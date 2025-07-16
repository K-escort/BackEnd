package com.sw.escort.chat.service;

import com.sw.escort.chat.dto.res.ChatResponse;

import java.util.List;

public interface ChatTopicService {
    List<ChatResponse.TopicResponseDto> getTopicsForUser(Long userId);
}

