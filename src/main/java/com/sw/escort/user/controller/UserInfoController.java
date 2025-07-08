package com.sw.escort.user.controller;

import com.sw.escort.apiPayload.ApiResponse;
import com.sw.escort.apiPayload.code.exception.GeneralException;
import com.sw.escort.apiPayload.code.status.ErrorStatus;
import com.sw.escort.apiPayload.code.status.SuccessStatus;
import com.sw.escort.common.security.JwtTokenProvider;
import com.sw.escort.user.dto.req.UserDtoReq;
import com.sw.escort.user.dto.res.UserDtoRes;
import com.sw.escort.user.entity.User;
import com.sw.escort.user.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userinfo")
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "환자 정보 저장 또는 수정 (보호자도 가능)")
    @PutMapping("/{targetUserId}")
    public ApiResponse<String> updateUserInfo(
            @PathVariable Long targetUserId,
            @RequestBody UserDtoReq.UserInfoUpdateReq req
    ) {
        Long modifierId = jwtTokenProvider.getUserIdFromToken();
        userInfoService.updateUserInfo(req, targetUserId, modifierId);
        return ApiResponse.onSuccess("환자 정보를 저장했으요~");
    }

    @Operation(summary = "환자 정보 조회(환자 본인이 조회)")
    @GetMapping("/me")
    public ApiResponse<UserDtoRes.UserInfoRes> getMyInfo() {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(userInfoService.getUserInfo(userId));
    }

    @Operation(summary = "특정 환자 정보 조회")
    @GetMapping("/{userId}")
    public ApiResponse<UserDtoRes.UserInfoRes> getUserInfo(@PathVariable Long userId) {
        return ApiResponse.onSuccess(userInfoService.getUserInfo(userId));
    }
}

