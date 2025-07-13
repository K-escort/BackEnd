package com.sw.escort.media.controller;

import com.sw.escort.apiPayload.ApiResponse;
import com.sw.escort.media.dto.req.PublicDataPhotoDtoReq;
import com.sw.escort.media.service.PublicDataPhotoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public-data-photos")
public class PublicDataPhotoController {

    private final PublicDataPhotoService publicDataPhotoService;

    @Operation(summary = "파일명 리스트로 공공데이터 이미지 URL 반환(Python 용 API)")
    @PostMapping("/urls")
    public ApiResponse<List<String>> getPublicDataPhotoUrls(@RequestBody PublicDataPhotoDtoReq.PublicDataPhotoUrlReq req) {
        return ApiResponse.onSuccess(publicDataPhotoService.getImageUrls(req.getImageList()));
    }
}

