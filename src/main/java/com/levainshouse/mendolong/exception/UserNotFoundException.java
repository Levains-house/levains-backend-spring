package com.levainshouse.mendolong.exception;

public class UserNotFoundException extends IllegalArgumentException{

    public UserNotFoundException(String message) {
        super(message);
    }
}
