package com.example.jbnu.osj.demo.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

// null인 필드(data 또는 message)는 JSON에서 자동으로 빠짐
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private String status;   // "success" 또는 "error"
    private T data;          // 성공 시 결과 데이터
    private String message;  // 에러(또는 안내) 메시지

    // 성공 응답: { "status": "success", "data": ... }
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> res = new ApiResponse<>();
        res.status = "success";
        res.data = data;
        return res;
    }

    // 에러 응답: { "status": "error", "message": "..." }
    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> res = new ApiResponse<>();
        res.status = "error";
        res.message = message;
        return res;
    }

    // getter (JSON 변환에 필요)
    public String getStatus() { return status; }
    public T getData() { return data; }
    public String getMessage() { return message; }
}

