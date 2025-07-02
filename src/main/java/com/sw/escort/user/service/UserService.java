package com.sw.escort.user.service;

import com.sw.escort.user.dto.req.UserDtoReq;
import com.sw.escort.user.dto.res.KakaoUserInfoResponseDto;
import com.sw.escort.user.dto.res.UserDtoRes;
import com.sw.escort.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserDtoRes.UserLoginRes login(HttpServletRequest request, HttpServletResponse response, UserDtoReq.LoginReq loginDto);
    void logout(HttpServletRequest request, HttpServletResponse response, String accessToken);
    User kakaoSignup(KakaoUserInfoResponseDto userInfo);
    UserDtoRes.UserLoginRes kakaoLogin(HttpServletRequest request, HttpServletResponse response, User user);
    void setUser(Long userId, UserDtoReq.userProfileReq userProfileReq);
    UserDtoRes.userProfileRes getUserBasicInfo(Long userId);
}
