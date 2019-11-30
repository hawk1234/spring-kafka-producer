package com.mz.example.jackson.databind;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.mz.example.service.customization.descriptor.ValueTypeDescriptor;

/**
 * Used by REST request deserialization
 */
public class StringToValueTypeDescriptorConverter extends StdConverter<String, ValueTypeDescriptor<?>> {

    @Override
    public ValueTypeDescriptor<?> convert(String vtName) {
        return ValueTypeDescriptor.findValueTypeDescriptorBy(vtName);
    }
}
