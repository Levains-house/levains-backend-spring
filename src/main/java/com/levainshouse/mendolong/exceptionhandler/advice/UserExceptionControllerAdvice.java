package com.levainshouse.mendolong.exceptionhandler.advice;

import com.levainshouse.mendolong.exception.UserNotFoundException;
import com.levainshouse.mendolong.exceptionhandler.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class UserExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    private static final String TAG = "USER_EXCEPTION={}";

    @ResponseStatus
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> userNotFoundExHandler(UserNotFoundException ex){
        log.error(TAG, ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST.name(), ex.getMessage()));
    }

}
