package com.mz.example.jackson.databind;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.mz.example.service.customization.descriptor.ValueTypeDescriptor;

/**
 * Used by REST request serialization - tests
 */
public class ValueTypeDescriptorConverterToString extends StdConverter<ValueTypeDescriptor<?>, String> {

    @Override
    public String convert(ValueTypeDescriptor<?> valueTypeDescriptor) {
        return valueTypeDescriptor.getValueType().name();
    }
}
