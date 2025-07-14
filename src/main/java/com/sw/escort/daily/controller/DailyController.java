package com.sw.escort.daily.controller;

import com.sw.escort.apiPayload.ApiResponse;
import com.sw.escort.common.security.JwtTokenProvider;
import com.sw.escort.daily.dto.req.DailyDtoReq;
import com.sw.escort.daily.dto.res.DailyDtoRes;
import com.sw.escort.daily.service.DailyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/record")
public class DailyController {
    private final DailyService dailyService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "데일리 기록 세부 조회 API", description = "데일리 기록의 세부 사항을 조회합니다")
    @GetMapping("/get/daily")
    public ApiResponse<DailyDtoRes.DailyRes> getdaily(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(dailyService.getDaily(userId, date));
    }

    @Operation(summary = "먼슬리 기록 조회 API", description = "데일리 월간 기록을 조회합니다")
    @GetMapping("/get/monthly")
    public ApiResponse<List<DailyDtoRes.MonthlyRes>> getmonthly(@RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth date) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(dailyService.getMonthly(userId, date));
    }

    @Operation(summary = "데일리 기록 저장 API", description = "사용자가 데일리 기록을 저장합니다.")
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> postDaily(
            @RequestPart("daily") @Valid DailyDtoReq.RecordDailyReq dailyDtoReq,
            @RequestPart(value = "dailyImages", required = false) List<MultipartFile> dailyImages,
            @RequestPart(value = "dailyVideos", required = false) List<MultipartFile> dailyVideos) throws IOException {

        Long userId = jwtTokenProvider.getUserIdFromToken();
        dailyService.saveDaily(userId, dailyDtoReq, dailyImages,dailyVideos);
        return ApiResponse.onSuccess("데일리 기록 저장완료");
    }

//    @Operation(summary = "데일리 기록 수정 API", description = "사용자가 데일리 기록을 저장합니다.")
//    @PutMapping(value = "/update/{dailyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ApiResponse<String> updateDaily(
//            @RequestPart("daily") DailyDTOReq.UpdateRecordDailyReq dailyDtoReq,
//            @PathVariable Long dailyId,
//            @RequestPart(value = "dailyImages", required = false) List<MultipartFile> dailyImages) throws IOException {
//
//        Long userId = jwtTokenProvider.getUserIdFromToken();
//        dailyService.updateDaily(userId, dailyId, dailyDtoReq, dailyImages);
//        return ApiResponse.onSuccess("데일리 기록 수정완료");
//    }
}
