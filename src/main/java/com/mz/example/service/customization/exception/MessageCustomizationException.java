package com.mz.example.service.customization.exception;

public class MessageCustomizationException extends RuntimeException{

    public MessageCustomizationException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
