package com.sw.escort.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.escort.apiPayload.ApiResponse;
import com.sw.escort.apiPayload.code.status.ErrorStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

//HTTP 요청 시 들어온 JWT 토큰을 검증하여 인증 처리 및 SecurityContext 에 인증 정보 저장
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = jwtTokenProvider.resolveAccessToken(); // 요청 헤더에서 액세스 토큰 추출 (Bearer 제거 후 토큰 추출)
        log.info("JwtAuthenticationFilter 토큰: {}", token);

    // 간결하게 인증 처리: 토큰 유효성 검증 + 블랙리스트 확인 + 인증 정보 주입
        if (jwtTokenProvider.processTokenAndSetAuthContext(token)) {
            // 인증 성공 시 SecurityContext에 등록 완료
        } else if (token != null) { // 유효하지 않거나 블랙리스트에 있을 경우 예외 응답 처리
            log.warn("무효화된 토큰이거나 잘못된 토큰입니다.");
            setErrorResponse((HttpServletResponse) response, ErrorStatus.JWT_INVALID);
            return;
        }

        filterChain.doFilter(request, response); // 다음 필터 또는 컨트롤러로 요청 전달
    }

    // 오류 응답 포맷 지정
    public static void setErrorResponse(HttpServletResponse response, ErrorStatus errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());
        ObjectMapper objectMapper = new ObjectMapper();

        ApiResponse<String> failureResponse = ApiResponse.onFailure(
                errorCode.getCode(),
                errorCode.getMessage()
        );

        String json = objectMapper.writeValueAsString(failureResponse);
        response.getWriter().write(json);
    }
}
