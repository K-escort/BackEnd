package com.sw.escort.media.controller;

import com.sw.escort.apiPayload.ApiResponse;
import com.sw.escort.common.security.JwtTokenProvider;
import com.sw.escort.media.dto.req.UserInfoPhotoDtoReq;
import com.sw.escort.media.dto.res.UserInfoPhotoDtoRes;
import com.sw.escort.media.service.UserInfoPhotoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-info-photos")
public class UserInfoPhotoController {

    private final UserInfoPhotoService userInfoPhotoService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "사용자 정보 사진 등록")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> upload(@RequestPart MultipartFile image,
                                      @RequestPart UserInfoPhotoDtoReq.Upload request) {
        Long modifierId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(userInfoPhotoService.uploadPhoto(request, image, modifierId));
    }

    @Operation(summary = "사용자 정보 사진 삭제")
    @DeleteMapping("/{photoId}")
    public ApiResponse<String> delete(@PathVariable Long photoId) {
        userInfoPhotoService.deletePhoto(photoId);
        return ApiResponse.onSuccess("사진 삭제 완료");
    }

    @Operation(summary = "사용자 정보 사진 정보 수정", description = "사진 수정 x, 사진 정보만 수정")
    @PatchMapping("/{photoId}")
    public ApiResponse<String> update(@PathVariable Long photoId,
                                      @RequestBody UserInfoPhotoDtoReq.UpdateInfo request) {
        Long modifierId = jwtTokenProvider.getUserIdFromToken();
        userInfoPhotoService.updatePhotoInfo(photoId, request, modifierId);
        return ApiResponse.onSuccess("사진 수정 완료");
    }

    @Operation(summary = "사용자 정보 사진 전체 조회")
    @GetMapping("/{userInfoId}")
    public ApiResponse<List<UserInfoPhotoDtoRes.Detail>> getPhotos(@PathVariable Long userInfoId) {
        return ApiResponse.onSuccess(userInfoPhotoService.getPhotos(userInfoId));
    }
}