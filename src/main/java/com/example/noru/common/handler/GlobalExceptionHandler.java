package com.example.noru.common.handler;


import com.example.noru.common.exception.CompanyException;
import com.example.noru.common.exception.NewsException;
import com.example.noru.common.response.ApiResponse;
import com.example.noru.common.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException e) {
        log.error("[INTERNAL_SERVER_ERROR]", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ResponseCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(NewsException.class)
    public ResponseEntity<ApiResponse<?>> handleNewsException(NewsException e) {

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ApiResponse.error(e.getResponseCode()));
    }

    @ExceptionHandler(CompanyException.class)
    public ResponseEntity<ApiResponse<?>> handleNewsException(CompanyException e) {

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ApiResponse.error(e.getResponseCode()));
    }
}