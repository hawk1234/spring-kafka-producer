package com.mz.example.service.customization.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.mz.example.service.customization.MessageCustomizationService;
import com.mz.example.service.customization.MessageType;
import com.mz.example.service.customization.descriptor.CustomizationDescriptor;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JSONMessageCustomizationService implements MessageCustomizationService {

    private static final ObjectMapper JSON_FORMATTER = new ObjectMapper();

    static {
        JSON_FORMATTER.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public String customizeMessage(String input, List<CustomizationDescriptor<?>> descriptors) {
        final DocumentContext documentContext = JsonPath.parse(input);
        descriptors.forEach(desc -> documentContext.set(desc.getValuePath(), desc.generateValue()));
        return formatJSON(documentContext.jsonString());
    }

    private String formatJSON(String jsonInput){
        try {
            Object jsonOutput = JSON_FORMATTER.readValue(jsonInput, Object.class);
            return JSON_FORMATTER.writeValueAsString(jsonOutput);
        } catch (JsonProcessingException ex) {
            log.warn("Couldn't format json", ex);
            return jsonInput;
        }
    }

    @Override
    public Set<MessageType> getSupportedMessageTypes() {
        return Collections.singleton(MessageType.JSON);
    }
}
