package com.sw.escort.daily.controller;

import com.sw.escort.apiPayload.ApiResponse;
import com.sw.escort.common.security.JwtTokenProvider;
import com.sw.escort.daily.dto.req.DailyDtoReq;
import com.sw.escort.daily.service.DailyImageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/record")
public class DailyImageController {
    private final DailyImageService dailyImageService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "선택된 사용자 정보 사진으로 AI 이미지 생성")
    @PostMapping("/generate-ai-images")
    public ApiResponse<List<String>> generateDailyImages(@RequestBody DailyDtoReq.DailyImageGenerationReq req) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(dailyImageService.generateAiImages(req, userId));
    }
}
