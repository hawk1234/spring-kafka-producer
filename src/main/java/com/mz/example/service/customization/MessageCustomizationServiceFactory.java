package com.mz.example.service.customization;

import com.mz.example.api.exception.UnsupportedMessageType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageCustomizationServiceFactory {

    @Autowired
    private List<MessageCustomizationService> messageCustomizationServices;

    public MessageCustomizationService getMessageCustomizationService(MessageType messageType){
        return messageCustomizationServices.stream()
                .filter(service -> service.getSupportedMessageTypes().contains(messageType))
                .findFirst()
                .orElseThrow(() -> new UnsupportedMessageType(messageType));
    }
}
