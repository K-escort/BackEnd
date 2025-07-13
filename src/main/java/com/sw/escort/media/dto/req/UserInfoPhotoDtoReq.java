package com.sw.escort.media.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

public class UserInfoPhotoDtoReq {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Upload {
        @NotNull(message = "UserInfo ID는 필수입니다.")
        @Schema(description = "유저 정보 Id, 특정 유저 정보 조회로 userInfoId 받을 수 있음")
        private Long userInfoId;

        @NotBlank(message = "사진 설명은 필수입니다.")
        @Schema(description = "사진 설명")
        private String description;

        @NotBlank(message = "관계는 필수입니다.")
        @Schema(description = "사진 인물과 환자와의 관계")
        private String relationToPatient;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateInfo {
        @NotBlank(message = "사진 설명은 필수입니다.")
        private String description;

        @NotBlank(message = "관계는 필수입니다.")
        private String relationToPatient;
    }
}
