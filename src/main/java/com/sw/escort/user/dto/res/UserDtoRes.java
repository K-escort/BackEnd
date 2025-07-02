package com.sw.escort.user.dto.res;

import com.sw.escort.user.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class UserDtoRes {

    @Data
    @AllArgsConstructor
    @Builder
    public static class UserLoginRes {
        private Long id;
        private String email;
        private String accessToken;
        private String refreshToken;
        private String name;
    }
    @Data
    @AllArgsConstructor
    @Builder
    public static class userProfileRes{
//        private String profileImageUrl;
        private String name;
        private Integer birthYear;
        private Role role;
        private String email;
    }
}
