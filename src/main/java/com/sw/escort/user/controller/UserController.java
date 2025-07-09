package com.sw.escort.user.controller;

import com.sw.escort.apiPayload.ApiResponse;
import com.sw.escort.apiPayload.code.exception.GeneralException;
import com.sw.escort.apiPayload.code.status.ErrorStatus;
import com.sw.escort.apiPayload.code.status.SuccessStatus;
import com.sw.escort.common.security.JwtTokenProvider;
import com.sw.escort.user.dto.req.AccessTokenRequest;
import com.sw.escort.user.dto.req.UserDtoReq;
import com.sw.escort.user.dto.res.KakaoUserInfoResponseDto;
import com.sw.escort.user.dto.res.UserDtoRes;
import com.sw.escort.user.entity.User;
//import com.sw.escort.user.service.KakaoService;
import com.sw.escort.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
//    private final KakaoService kakaoService;

//    @Operation(summary = "앱 카카오로그인 API", description = "앱에서 카카오 로그인")
//    @PostMapping("/kakao-login")
//    public ApiResponse<UserDtoRes.UserLoginRes> kakaoLogin(@RequestBody @Valid AccessTokenRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
//        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(request.getAccessToken());
//        User user = userService.kakaoSignup(userInfo);
//        return ApiResponse.onSuccess(userService.kakaoLogin(httpRequest, httpResponse, user));
//    }

    @Operation(summary = "이메일 로그인 API(테스트용)", description = "이메일로 JWT토큰 발급")
    @PostMapping("/login")
    public ApiResponse<UserDtoRes.UserLoginRes> login(@RequestBody @Valid UserDtoReq.LoginReq loginDto, HttpServletRequest request, HttpServletResponse response) {
        return ApiResponse.onSuccess(userService.login(request,response,loginDto));
    }

    @Operation(summary = "로그아웃 API", description = "액세스 토큰을 무효화하여 로그아웃")
    @PostMapping("/logout")
    public ApiResponse<SuccessStatus> logout(
            @RequestHeader(value = "Authorization", required = false) String accessToken,
            HttpServletRequest request, HttpServletResponse response) {

        userService.logout(request, response, accessToken);
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    @Operation(summary = "웹용-로그아웃 API", description = "액세스 토큰을 무효화하고 쿠키 삭제")
    @PostMapping("/logout/web")
    public ApiResponse<String> logoutWeb(
            @RequestHeader(value = "Authorization", required = false) String accessToken,
            HttpServletRequest request, HttpServletResponse response) {

        userService.logoutWeb(request, response, accessToken);
        return ApiResponse.onSuccess("로그아웃 성공이요~");
    }

    @Operation(summary = "토큰 재발급 API", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰 발급")
    @PostMapping("/refresh")
    public ApiResponse<Map<String, String>> refresh(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.resolveRefreshToken();
        String accessToken = jwtTokenProvider.resolveAccessToken();

        if (refreshToken == null) {
            throw new GeneralException(ErrorStatus.JWT_EMPTY);
        }

        Long userId = jwtTokenProvider.getUserIdInToken(refreshToken);

        // 리프레시 토큰 검증
        if (!jwtTokenProvider.validateRefreshToken(refreshToken, userId)) {
            throw new GeneralException(ErrorStatus.JWT_REFRESHTOKEN_NOT_MATCHED);
        }

        //  헬퍼 메서드로 로그아웃 처리 간소화
        jwtTokenProvider.handleLogout(accessToken, refreshToken);

        // 새로운 액세스 토큰 & 리프레시 토큰 발급
        String newAccessToken = jwtTokenProvider.createAccessToken(userId);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(userId);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", newRefreshToken);

        return ApiResponse.onSuccess(tokens);
    }

    @Operation(summary = "회원정보 설정/수정 API", description = "닉네임,출생년도,신분,입시유형을 수정합니다.")
    @PatchMapping ("")
    public ApiResponse<String> updateUserProfile(@RequestBody @Valid UserDtoReq.userProfileReq userProfileReq) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        userService.setUser(userId, userProfileReq);
        return ApiResponse.onSuccess("회원정보 수정 완료");
    }

    @Operation(summary = "회원 기본 정보 조회 API", description = "사용자의 기본 정보를 조회합니다.")
    @GetMapping("/basic-info")
    public ApiResponse<UserDtoRes.userProfileRes> getUserBasicInfo() {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        UserDtoRes.userProfileRes userBasicInfo = userService.getUserBasicInfo(userId);
        return ApiResponse.onSuccess(userBasicInfo);
    }

}
