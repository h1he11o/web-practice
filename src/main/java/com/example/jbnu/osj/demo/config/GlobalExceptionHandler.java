package com.example.jbnu.osj.demo.config;

import com.example.jbnu.osj.demo.api.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

// 컨트롤러에서 예외가 터져도 { "status": "error", "message": ... } 형태로 통일해서 응답
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiResponse<?>> fail(int code, String message) {
        return ResponseEntity.status(code).body(ApiResponse.error(message));
    }

    // 400: JSON 문법 오류 (쉼표 빠짐, 따옴표 오류 등)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleBadJson(HttpMessageNotReadableException e) {
        return fail(400, "요청 본문(JSON) 형식이 올바르지 않습니다.");
    }

    // 400: 잘못된 값 (예: /items/abc 처럼 id에 문자가 들어온 경우)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse<?>> handleBadArgument(Exception e) {
        return fail(400, "요청 값이 올바르지 않습니다.");
    }

    // 404: 존재하지 않는 주소
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoResource(NoResourceFoundException e) {
        return fail(404, "존재하지 않는 주소입니다.");
    }

    // 405: 주소는 있지만 해당 메서드를 지원하지 않음
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        return fail(405, "지원하지 않는 HTTP 메서드입니다.");
    }

    // 415: Content-Type이 JSON이 아님
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleUnsupportedMedia(HttpMediaTypeNotSupportedException e) {
        return fail(415, "Content-Type은 application/json 이어야 합니다.");
    }

    // 500: 그 외 예상하지 못한 모든 서버 오류
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleUnexpected(Exception e) {
        return fail(500, "서버 내부 오류가 발생했습니다.");
    }
}

