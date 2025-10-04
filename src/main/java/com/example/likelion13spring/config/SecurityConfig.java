package com.example.likelion13spring.config;

import com.example.likelion13spring.service.CustomOAuth2UserService;
import com.example.likelion13spring.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration //<- Spring 설정 파일로 간주, 내부의 메서드들은 자동으로 Bean으로 등록됨
@EnableWebSecurity //<- 해당 설정을 읽어 필터 체인 생성, 웹 보안 활성화
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService; // UserDetailsService DI. 의존성 주입
    private final CustomOAuth2UserService customOAuth2UserService; // week26 추가

    private static void corsAllow(CorsConfigurer<HttpSecurity> corsConfigurer) {
        corsConfigurer.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();

            configuration.setAllowedMethods(Collections.singletonList("*")); // *로 모든 메서드 허용 (POST, GET 등으로 제한 가능)
            configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 허용할 origin 설정. 여기선 프론트에서 오는 요청 허용
            configuration.setAllowedHeaders(Collections.singletonList("*")); // 허용할 HTTP request header 설정. 여기선 모든 헤더 허용
            configuration.setAllowCredentials(true); //자격 증명 허용 어부 (기본 값은 false)
            configuration.setMaxAge(3600L); // cors 응답 캐싱 시간 (동일한 origin에서 들어온 요청 처리 성능 향상). 여기선 1시간(3600초) 동안 오는 요청이 처리됨

            return configuration;
        });
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 설정 추가
        http
                .cors((SecurityConfig::corsAllow)) //CORS 설정
                .csrf(AbstractHttpConfigurer::disable) //csrf 공격 방지 비활성화 <- REST API는 HTTP 형식을 따라서 무상태(세션, 쿠키에 의존하지 않음)라서 ㄱㅊ
//                .authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/join", "/login").permitAll() // join, login으로 오는 것(회원가입, 로그인)은 모두 허용 <- 로그인 전에도 사용가능
//                        .requestMatchers("/**").authenticated()) // 나머지는 인증된 사용자만 허용 <- 나머지는 로그인해야 사용가능
//                .formLogin(Customizer.withDefaults()) //이거 디폴트로 사용하려면 로그인을 name을 username? 으로 해둬야 함 @@ 뭐지 이건
//                .logout(Customizer.withDefaults())
                .authorizeHttpRequests((auth) -> auth
                    .requestMatchers("/join", "/login",
                        "/oauth2/**", "/login/oauth2/**",
                        "/h2-console/**", "/error").permitAll()
                    .anyRequest().authenticated())
                    .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                    )
                .userDetailsService(customUserDetailsService)
        ;
        return http.build();

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
