package com.example.noru.common.response;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS_SIGN_UP("SUCCESS_SIGN_UP", "회원가입에 성공하였습니다.", HttpStatus.CREATED),
    SUCCESS_LOGIN("SUCCESS_LOGIN", "로그인에 성공하였습니다.", HttpStatus.OK),

    STOCK_NOT_FOUND("STOCK_NOT_FOUND", "해당하는 종목이 없습니다.", HttpStatus.NOT_FOUND),

    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);


    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}