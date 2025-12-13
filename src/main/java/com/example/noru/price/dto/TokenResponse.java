package com.example.noru.price.dto;

public record TokenResponse(
        String access_token,
        int expires_in
) {
    public String accessToken() {
        return access_token;
    }
    public int expiresIn() {
        return expires_in;
    }
}
