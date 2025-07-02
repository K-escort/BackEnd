package com.sw.escort.user.controller;

import com.sw.escort.apiPayload.ApiResponse;
import com.sw.escort.user.dto.res.KakaoUserInfoResponseDto;
import com.sw.escort.user.dto.res.UserDtoRes;
import com.sw.escort.user.service.KakaoService;
import com.sw.escort.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class SocialLoginController {

    private final KakaoService kakaoService;
    private final UserService userService;

    @Deprecated
    @GetMapping("/kakao/callback")
    public ApiResponse<UserDtoRes.UserLoginRes> callback(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);

        //회원가입, 로그인 동시진행
        return ApiResponse.onSuccess(userService.kakaoLogin(request,response, userService.kakaoSignup(userInfo)));
    }
}
