package com.example.noru.common.exception;

import com.example.noru.common.response.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NewsException extends RuntimeException{
    private final ResponseCode responseCode;

    public NewsException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public HttpStatus getHttpStatus() {return responseCode.getHttpStatus();}
}
