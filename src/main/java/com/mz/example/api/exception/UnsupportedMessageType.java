package com.mz.example.api.exception;

import com.mz.example.service.customization.MessageType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedMessageType extends RuntimeException{

    public UnsupportedMessageType(MessageType msgType){
        super(produceErrorMessage(msgType));
    }

    public static String produceErrorMessage(MessageType vtName) {
        return "Unsupported message type: "+vtName;
    }
}
