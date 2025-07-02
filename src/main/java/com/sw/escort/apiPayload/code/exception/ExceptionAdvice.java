package com.sw.escort.apiPayload.code.exception;


import com.sw.escort.apiPayload.ApiResponse;
import com.sw.escort.apiPayload.code.ErrorReasonDTO;
import com.sw.escort.apiPayload.code.status.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(constraintViolation -> constraintViolation.getMessage())
                .findFirst()
                .orElse("ConstraintViolationException 발생");

        return handleSimpleException(e, ErrorStatus.INVALID_PAGE_PARAMETER, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(fieldError -> errors.put(fieldError.getField(),
                        Optional.ofNullable(fieldError.getDefaultMessage()).orElse("잘못된 입력입니다.")));

        return handleExceptionWithArgs(ex, ErrorStatus._BAD_REQUEST, request, errors);
    }

//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
//        if ("page".equals(ex.getName())) {
//            return handleSimpleException(ex, ErrorStatus.INVALID_PAGE_PARAMETER, request);
//        }
//        return handleSimpleException(ex, ErrorStatus._BAD_REQUEST, request);
//    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<Object> handleGeneralException(GeneralException ex, HttpServletRequest request) {
        ErrorReasonDTO reason = ex.getErrorReasonHttpStatus();
        return handleDetailedException(ex, reason, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception e, WebRequest request) {
        log.error("[UNHANDLED EXCEPTION]", e);
        return handleSimpleException(e, ErrorStatus._INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Object> handleDetailedException(Exception e, ErrorReasonDTO reason,
                                                           HttpServletRequest request) {
        ApiResponse<Object> body = ApiResponse.onFailure(reason.getCode(), reason.getMessage());
        WebRequest webRequest = new ServletWebRequest(request);
        return super.handleExceptionInternal(e, body, HttpHeaders.EMPTY, reason.getHttpStatus(), webRequest);
    }

    private ResponseEntity<Object> handleSimpleException(Exception e, ErrorStatus errorStatus, WebRequest request) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorStatus.getCode(), errorStatus.getMessage());
        return super.handleExceptionInternal(e, body, HttpHeaders.EMPTY, errorStatus.getHttpStatus(), request);
    }

    private ResponseEntity<Object> handleExceptionWithArgs(Exception e, ErrorStatus errorStatus, WebRequest request, Map<String, String> errorArgs) {
        ApiResponse<Object> body = ApiResponse.onFailure(errorStatus.getCode(), errorStatus.getMessage());
        return super.handleExceptionInternal(e, body, HttpHeaders.EMPTY, errorStatus.getHttpStatus(), request);
    }
}
