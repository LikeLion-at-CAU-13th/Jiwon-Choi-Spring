package com.example.likelion13spring.dto.response;

//accesstoken, refreshtoken을 가져와서 새로운 토큰 response dto를 만들어줌
public record TokenResponseDto (
        String accessToken,
        String refreshToken) {

    public static TokenResponseDto of(String accessToken, String refreshToken) {
        return new TokenResponseDto(accessToken, refreshToken);
    }
}