package com.sw.escort.daily.controller;

import com.sw.escort.apiPayload.ApiResponse;
import com.sw.escort.common.security.JwtTokenProvider;
import com.sw.escort.daily.dto.req.DailyDtoReq;
import com.sw.escort.daily.dto.res.DailyDtoRes;
import com.sw.escort.daily.service.DailyImageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/record")
public class DailyImageController {
    private final DailyImageService dailyImageService;

    @Operation(summary = "AI 이미지 생성 (유저의 전체 사진 기반)")
    @PostMapping("/generate-ai-images")
    public ApiResponse<List<DailyDtoRes.DailyImageUploadRes>> generateDailyImages(@RequestBody DailyDtoReq.DailyImageGenerationReq req) {
        return ApiResponse.onSuccess(dailyImageService.generateAiImages(req.getDailyId()));
    }
}
