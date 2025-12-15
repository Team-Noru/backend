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
    SUCCESS_NEWS_COMPANY("SUCCESS_NEWS_COMPANY", "특정 기업 뉴스 목록 조회에 성공하였습니다.", HttpStatus.OK),
    SUCCESS_NEWS_DETAIL("SUCCESS_NEWS_DETAIL", "뉴스 상세 조회에 성공하였습니다.", HttpStatus.OK),
    SUCCESS_ANNOUNCEMENT("SUCCESS_ANNOUNCEMENT", "해당 기업 공시 이슈 조회에 성공하였습니다.", HttpStatus.OK),
    SUCCESS_COMPANY_DETAIL("SUCCESS_COMPANY_DETAIL", "기업 상세 조회에 조회에 성공하였습니다.", HttpStatus.OK),
    SUCCESS_WORD_CLOUD("SUCCESS_WORD_CLOUD", "특정 기업 워드 클라우드 조회에 성공하였습니다.", HttpStatus.OK),
    SUCCESS_PRICE("SUCCESS_PRICE", "현재가 조회에 성공하였습니다.", HttpStatus.OK),


    NEWS_NOT_FOUND("NEWS_NOT_FOUND", "뉴스 데이터가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    COMPANY_NOT_FOUND("COMPANY_NOT_FOUND", "기업이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    ANNOUNCEMENT_NOT_FOUND("ANNOUNCEMENT_NOT_FOUND", "해당 기업 공시 이슈가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    COMPANY_RELATION_NOT_FOUND("COMPANY_RELATION_NOT_FOUND", "연관 관계 기업이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    WORD_CLOUD_NOT_FOUND("WORD_CLOUD_NOT_FOUND", "워드클라우드 데이터가 없습니다.", HttpStatus.NOT_FOUND),

    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);


    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}