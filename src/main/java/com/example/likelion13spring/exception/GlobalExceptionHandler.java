package com.example.likelion13spring.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //이 클래스가 전역에서 REST컨트롤러 예외를 처리하는 핸들러라는 뜻
public class GlobalExceptionHandler {

    //유효성 검사 실패, 잘못된 요청 데이터 등의 처리... 주문 조회, 수정 등에서 IllegalArgumentException 발생 시 400
    //@@ 근데 이거 제대로 작동하는지 다시 확인!
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), //400
                ex.getMessage(), //메시지
                request.getRequestURI() //요청경로
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    //회원가입 시 중복 이름 등 리소스 충돌 처리용!
    //일단 IllegalStateException, 409 CONFLICT로 처리했음
    @ExceptionHandler(IllegalStateException.class)
    // 예외 객체, 예외가 발생한 HTTP 요청 정보를 파라미터로 받아옴
    public ResponseEntity<ErrorResponse> handleConflict(IllegalStateException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(), // 409 CONFLICT
                ex.getMessage(),
                request.getRequestURI() //예외가 발생한 http 요청경로
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    public record ErrorResponse(
            int status,
            String message,
            String path
    ) {}
}