package com.sw.escort.user.dto.req;

import com.sw.escort.user.entity.enums.CognitiveStatus;
import com.sw.escort.user.entity.enums.Gender;
import com.sw.escort.user.entity.enums.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class UserDtoReq {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginReq {
        @NotBlank(message = "이메일은 필수입니다.")
        private String email;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class userProfileReq {
        @NotNull(message = "출생 년도는 필수입니다.")
        @Min(value = 1900, message = "출생 년도는 1900년 이후여야 합니다.")
        @Max(value = 2025, message = "출생 년도는 현재 연도 이하여야 합니다.")
        private Integer birthYear;
        @NotNull(message = "역할은 필수입니다.")
        private Role role;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfoUpdateReq {
        private Integer age;
        private Gender gender;
        private CognitiveStatus cognitiveStatus;
        private String hometown;
        private String lifeHistory;
        private String familyInfo;
        private String education;
        private String occupation;
        private String forbiddenKeywords;
        private String lifetimeline;
    }
}
