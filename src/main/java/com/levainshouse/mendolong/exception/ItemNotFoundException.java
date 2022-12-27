package com.levainshouse.mendolong.exception;

public class ItemNotFoundException extends IllegalArgumentException{

    public ItemNotFoundException(String message) {
        super(message);
    }
}
