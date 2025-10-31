package com.example.likelion13spring.jwt;

import com.example.likelion13spring.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

//27주차 jwt
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    // ACCESS_TOKEN : 1분
    private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 1000L * 60;
    // REFRESH_TOKEN : 7일
    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 7;
    private final CustomUserDetailsService customUserDetailsService;
    @Value("${jwt.secret}")
    private String JWT_SECRET;

    // access token 생성하는 함수 - username으로 가져옴
    public String generateAccessToken(String username) {
        final Date now = new Date();

        //시간, Expiration 정보를 넣어줌
        final Claims claims = Jwts.claims()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME));

        //여기에 username을 넣어줌
        claims.put("username", username); // username으로 회원을 구분한다고 가정하고 만들어보겠습니다. memberId로 만들기도 합니다!

        //위에서 만든 걸 builder를 통해
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // header
                .setClaims(claims) // claim
                .signWith(getSigningKey()) // signature
                .compact();
    }


    // refresh token 생성
    public String generateRefreshToken(String username) {
        final Date now = new Date();
        final Claims claims = Jwts.claims()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME));

        claims.put("username", username);

        // refresh token은 db나 cache server에 저장하기도 합니다

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .signWith(getSigningKey())
                .compact();
    }

    // token 유효성 확인 - 토큰을 받아 파싱 시도 후 성공하면 제대로 된 타입을 반환, 그렇지 않으면 해당하는 타입을 반환함
    public JwtValidationType validateToken(String token) {
        try {
            getBody(token); // 유효성 검사를 위해 파싱 시도
            return JwtValidationType.VALID_JWT;
        } catch (MalformedJwtException ex) { // 잘못된 형식의 JWT 토큰인 경우 -> 헤더.페이로드.서명 형식이 아니거나 토큰 문자열이 손상되었을 때
            return JwtValidationType.INVALID_JWT_TOKEN;
        } catch (ExpiredJwtException ex) { // 토큰의 유효 기간이 만료된 경우 -> 보통 해당 에러가 날 경우 reissue 처리하는 경우도 있답니다
            return JwtValidationType.EXPIRED_JWT_TOKEN;
        } catch (UnsupportedJwtException ex) { // 지원하지 않는 JWT 토큰이 전달되는 경우 -> 다른 서명 알고리즘이 사용된 JWT가 전달될 경우
            return JwtValidationType.UNSUPPORTED_JWT_TOKEN;
        } catch (IllegalArgumentException ex) { // 빈 문자열 같이 유효하지 않은 문자열로 JWT 파싱이 시도되는 경우
            return JwtValidationType.EMPTY_JWT;
        }
    }

    //accesstoken으로 username 반환하는 함수
    public String getUsernameFromAccessToken(String token) {
        Claims claims = getBody(token);
        return claims.get("username").toString(); //String으로 바꿔 반환함
    }

    private Claims getBody(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey getSigningKey() {
        String encodedKey = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes()); //SecretKey 통해 서명 생성
        return Keys.hmacShaKeyFor(encodedKey.getBytes());   //일반적으로 HMAC (Hash-based Message Authentication Code) 알고리즘 사용
    }
}
