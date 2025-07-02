package com.sw.escort.user.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccessTokenRequest {
    @NotBlank(message = "엑세스 토큰은 필수입니다.")
    private String accessToken;
}