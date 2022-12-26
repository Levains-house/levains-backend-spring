package com.levainshouse.mendolong.exceptionhandler.advice;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.levainshouse.mendolong.exceptionhandler.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
@Order(0)
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    private static final String TAG = "CONTROLLER_EXCEPTION={}";

    @ResponseStatus
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ExceptionResponse> tokenExpiredExHandler(TokenExpiredException ex){
        log.error(TAG, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(HttpStatus.UNAUTHORIZED.name(), "토큰이 만료되었습니다."));
    }

    @ResponseStatus
    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<ExceptionResponse> jwtDecodedExHandler(JWTDecodeException ex){
        log.error(TAG, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(HttpStatus.UNAUTHORIZED.name(), "부적절한 토큰 형식입니다."));
    }

    @ResponseStatus
    @ExceptionHandler(SignatureVerificationException.class)
    public ResponseEntity<ExceptionResponse> signatureVerificationExHandler(SignatureVerificationException ex){
        log.error(TAG, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(HttpStatus.UNAUTHORIZED.name(), "인가받지 않은 토큰입니다."));
    }

    @ResponseStatus
    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ExceptionResponse> illegalExHandler(JWTVerificationException ex){
        log.error(TAG, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(HttpStatus.UNAUTHORIZED.name(), "검증되지 않은 토큰입니다."));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(TAG, ex.getMessage());

        ex.getBindingResult().getAllErrors();
        String message = ex.getBindingResult().getFieldError().getDefaultMessage() == null? ""
                :ex.getBindingResult().getFieldError().getDefaultMessage();

        return ResponseEntity
                .badRequest()
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST.name(), message));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(TAG, ex.getMessage());
        String message = ex.getMessage().split(":")[0];

        return ResponseEntity
                .badRequest()
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST.name(), message));
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(TAG, ex.getMessage());
        String message = ex.getMessage().split(":")[0];

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST.name(), message));
    }

    @ResponseStatus
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> illegalExHandler(IllegalArgumentException ex){
        log.error(TAG, ex.getMessage());
        String message = ex.getMessage().split(":")[0];
        return ResponseEntity
                .badRequest()
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST.name(), message));
    }

    @ResponseStatus
    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> exHandler(Exception ex){
        log.error(TAG, ex.getMessage());
        String message = ex.getMessage().split(":")[0];
        return ResponseEntity
                .internalServerError()
                .body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), message));
    }
}
