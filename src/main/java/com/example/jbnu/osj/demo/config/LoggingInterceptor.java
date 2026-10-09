package com.example.jbnu.osj.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

// 미들웨어: 요청이 컨트롤러에 도착하기 전/후에 공통으로 실행됨
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);
    private static final String START_TIME = "startTime";

    // 컨트롤러 실행 "전": 요청 정보 로그
    // 출력 예: [REQUEST ] POST /api/v1/items
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME, System.currentTimeMillis());

        String query = request.getQueryString();
        log.info("[REQUEST ] {} {}{}",
                request.getMethod(),
                request.getRequestURI(),
                query != null ? "?" + query : "");

        return true; // true: 컨트롤러로 통과 / false: 여기서 차단
    }

    // 응답이 끝난 "후": 상태 코드와 처리 시간 로그
    // 출력 예: [RESPONSE] POST /api/v1/items -> 201 (5ms)
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        Long start = (Long) request.getAttribute(START_TIME);
        long elapsed = (start != null) ? System.currentTimeMillis() - start : -1;

        log.info("[RESPONSE] {} {} -> {} ({}ms)",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                elapsed);
    }
}