package com.example.noru.common.response;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS_SIGN_UP("SUCCESS_SIGN_UP", "회원가입에 성공하였습니다.", HttpStatus.CREATED),
    SUCCESS_LOGIN("SUCCESS_LOGIN", "로그인에 성공하였습니다.", HttpStatus.OK),


    SUCCESS_NEWS_LIST("SUCCESS_NEWS_LIST", "해당 날짜 뉴스 목록 조회에 성공하였습니다.", HttpStatus.OK),
    SUCCESS_NEWS_DETAIL("SUCCESS_NEWS_DETAIL", "뉴스 상세 조회에 성공하였습니다.", HttpStatus.OK),
    SUCCESS_ANNOUNCEMENT("SUCCESS_ANNOUNCEMENT", "해당 기업 공시 이슈 조회에 성공하였습니다.", HttpStatus.OK),


    NEWS_NOT_FOUND("NEWS_NOT_FOUND", "뉴스 데이터가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    ANNOUNCEMENT_NOT_FOUND("ANNOUNCEMENT_NOT_FOUND", "해당 기업 공시 이슈가 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);


    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}