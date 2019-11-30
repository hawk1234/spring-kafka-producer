package com.mz.example.service.customization;

import com.mz.example.service.customization.descriptor.CustomizationDescriptor;

import java.util.List;
import java.util.Set;

public interface MessageCustomizationService {

    String customizeMessage(String input, List<CustomizationDescriptor<?>> descriptors);
    Set<MessageType> getSupportedMessageTypes();
}
