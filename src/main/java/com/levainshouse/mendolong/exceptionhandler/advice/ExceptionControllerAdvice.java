package com.levainshouse.mendolong.exceptionhandler.advice;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.levainshouse.mendolong.exceptionhandler.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    private static final String TAG = "CONTROLLER_EXCEPTION={}";

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

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ExceptionResponse> authExHandler(JWTVerificationException ex){
        log.error(TAG, ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(HttpStatus.UNAUTHORIZED.name(), "인가받지 않은 사용자입니다."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> illegalExHandler(IllegalArgumentException ex){
        log.error(TAG, ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST.name(), ex.getMessage()));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ExceptionResponse> ioExHandler(IOException ex){
        log.error(TAG, ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST.name(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> exHandler(Exception ex){
        log.error(TAG, ex.getMessage());
        String message = ex.getMessage().split(":")[0];
        return ResponseEntity
                .internalServerError()
                .body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), message));
    }
}
