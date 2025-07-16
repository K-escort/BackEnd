package com.sw.escort.daily.controller;

import com.sw.escort.apiPayload.ApiResponse;
import com.sw.escort.common.security.JwtTokenProvider;
import com.sw.escort.daily.dto.res.DailyDtoRes;
import com.sw.escort.daily.service.DailyConversationService;
import com.sw.escort.daily.service.DailyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/conversation")
public class DailyConversationController {
    private final DailyConversationService dailyConversationService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "대화 기록 저장 API", description = "대화 기록을 저장합니다")
    @PostMapping("/save")
    public ApiResponse<DailyDtoRes.DailyConversationRes> saveConversation(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(dailyConversationService.saveConversations(userId, date));
    }
}
