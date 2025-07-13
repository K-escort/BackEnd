package com.sw.escort.user.dto.res;

import com.sw.escort.user.entity.UserInfo;
import com.sw.escort.user.entity.enums.CognitiveStatus;
import com.sw.escort.user.entity.enums.Gender;
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
        private Long id;
        private String name;
        private Integer birthYear;
        private Role role;
        private String email;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class UserInfoRes {
        private Long userInfoId;
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
        private String lastModifiedBy;

        public static UserInfoRes from(UserInfo info) {
            return UserInfoRes.builder()
                    .userInfoId(info.getId())
                    .age(info.getAge())
                    .gender(info.getGender())
                    .cognitiveStatus(info.getCognitiveStatus())
                    .hometown(info.getHometown())
                    .lifeHistory(info.getLifeHistory())
                    .familyInfo(info.getFamilyInfo())
                    .education(info.getEducation())
                    .occupation(info.getOccupation())
                    .forbiddenKeywords(info.getForbiddenKeywords())
                    .lifetimeline(info.getLifetimeline())
                    .lastModifiedBy(info.getLastModifiedBy())
                    .build();
        }
    }
}
