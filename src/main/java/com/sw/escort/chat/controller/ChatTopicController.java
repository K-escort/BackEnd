package com.sw.escort.chat.controller;

import com.sw.escort.apiPayload.ApiResponse;
import com.sw.escort.chat.dto.res.ChatResponse;
import com.sw.escort.chat.service.ChatTopicService;
import com.sw.escort.common.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatTopicController {

    private final JwtTokenProvider jwtTokenProvider;
    private final ChatTopicService chatTopicService;

    @GetMapping("/topics")
    public ApiResponse<List<ChatResponse.TopicResponseDto>> getChatTopics() {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        List<ChatResponse.TopicResponseDto> topics = chatTopicService.getTopicsForUser(userId);
        return ApiResponse.onSuccess(topics);
    }
}
