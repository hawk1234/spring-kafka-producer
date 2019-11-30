package com.mz.example.thymeleaf.databind;

import com.mz.example.service.customization.descriptor.ValueTypeDescriptor;
import org.springframework.core.convert.converter.Converter;

/**
 * Used by Thymeleaf during form -> request conversion
 */
public class ValueTypeDescriptorConverter implements Converter<String, ValueTypeDescriptor<?>> {

    @Override
    public ValueTypeDescriptor<?> convert(String vtName) {
        return ValueTypeDescriptor.findValueTypeDescriptorBy(vtName);
    }
}
