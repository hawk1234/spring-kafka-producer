package com.mz.example.api.model;

import com.mz.example.service.customization.MessageType;
import com.mz.example.service.customization.descriptor.CustomizationDescriptor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProduceMessageRequest {
    private String topic;
    private String msg;
    private MessageType msgType;
    private int amount = 1;
    private List<CustomizationDescriptor<?>> customizationDescriptors = new ArrayList<>();
}
