package com.sw.escort.apiPayload.code.status;

import com.sw.escort.apiPayload.code.BaseErrorCode;
import com.sw.escort.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 에러 예시
    FAIL_OOOOO(HttpStatus.BAD_REQUEST, "FAIL", "실패하였습니다."),

    // 토큰 관련 에러
    JWT_AUTHORIZATION_FAILED(HttpStatus.UNAUTHORIZED, "JWT4001", "권한이 없습니다."),
    JWT_INVALID(HttpStatus.UNAUTHORIZED, "JWT4002", "유효하지 않은 토큰입니다."),
    JWT_EMPTY(HttpStatus.UNAUTHORIZED, "JWT4003", "JWT 토큰을 넣어주세요."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT4004", "만료된 토큰입니다."),
    JWT_REFRESHTOKEN_NOT_MATCHED(HttpStatus.UNAUTHORIZED, "JWT4005", "RefreshToken이 일치하지 않습니다."),
    JWT_REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "JWT4031", "리프레시 토큰이 존재하지 않거나 만료되었습니다."),
    JWT_REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "JWT4032", "유효하지 않은 리프레시 토큰입니다."),

    // 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    //User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4004", "사용자를 찾을 수 없습니다."),
    USER_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "USERINFO4004", "저장된 사용자 정보가 없습니다."),
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "USERINFO4001", "정보 수정 권한이 없습니다."),

    //Image
    FILE_TOO_LARGE(HttpStatus.BAD_REQUEST, "FILE4000", "이미지 용량은 5MB이하로 해주세요"),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST,"FILE4001","이미지 파일만 업로드해주세요" ),
    INVALID_IMAGE_URL(HttpStatus.NOT_FOUND,"FILE4002","이미지 URL을 찾을 수 없습니다"),
    FILE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "FILE4003", "파일 업로드에 실패했습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "FILE4004", "해당 이미지를 찾을 수 없습니다."),

    //paging
    INVALID_PAGE_PARAMETER(HttpStatus.BAD_REQUEST, "PAGE400", "잘못된 페이지 값입니다. 1 이상의 정수로 입력해주세요."),

    //관계
    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "관계 요청을 찾을 수 없습니다."),
    CANNOT_REQUEST_SELF(HttpStatus.BAD_REQUEST, "R003", "자기 자신에게 관계 요청을 보낼 수 없습니다."),
    ALREADY_RELATED(HttpStatus.CONFLICT, "R002", "이미 관계가 존재합니다."),
    CANNOT_SEARCH_SELF(HttpStatus.BAD_REQUEST, "U005", "자기 자신은 검색할 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
