package com.sw.escort.chat.controller;

import com.sw.escort.apiPayload.ApiResponse;
import com.sw.escort.chat.dto.req.ChatStartReq;
import com.sw.escort.chat.dto.res.ChatResponse;
import com.sw.escort.chat.service.ChatService;
import com.sw.escort.common.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final JwtTokenProvider jwtTokenProvider;
    private final ChatService chatService;

    @Operation(summary = "LLM 대화 요청")
    @PostMapping("/start")
    public ApiResponse<ChatResponse.ChatDetail> startChat(@RequestBody ChatStartReq req) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(chatService.startChat(userId, req.getUserPrompt()));
    }
}

