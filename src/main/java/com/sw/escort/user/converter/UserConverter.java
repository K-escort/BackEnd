package com.sw.escort.user.converter;

import com.sw.escort.user.dto.res.UserDtoRes;
import com.sw.escort.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter {
    public static UserDtoRes.UserLoginRes signInRes(User user, String accessToken, String refreshToken,String name) {
        return UserDtoRes.UserLoginRes.builder()
                .id(user.getId())
                .email(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .name(name)
                .build();
    }
    public static UserDtoRes.userProfileRes toUserProfileRes(User user) {

        return new UserDtoRes.userProfileRes(
                user.getName(),
                user.getBirthYear(),
                user.getRole(),
                user.getEmail()
        );
    }
}
