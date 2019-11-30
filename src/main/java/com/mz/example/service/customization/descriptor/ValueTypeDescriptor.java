package com.mz.example.service.customization.descriptor;

import com.mz.example.api.exception.UnsupportedValueType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValueTypeDescriptor<VALUE_TYPE> {

    public static final ValueTypeDescriptor<Number> NUMBER = new ValueTypeDescriptor<>(ValueType.NUMBER, Number.class);
    public static final ValueTypeDescriptor<String> STRING = new ValueTypeDescriptor<>(ValueType.STRING, String.class);

    private ValueType valueType;
    private Class<VALUE_TYPE> valueTypeClass;

    public enum ValueType {
        NUMBER,
        STRING
    }

    public static ValueTypeDescriptor<?> findValueTypeDescriptorBy(@NonNull ValueType valueType){
        return findValueTypeDescriptorBy(valueType.name());
    }

    public static ValueTypeDescriptor<?> findValueTypeDescriptorBy(@NonNull String name){
        name = name.toUpperCase();
        switch (name) {
            case "NUMBER":
                return NUMBER;
            case "STRING":
                return STRING;
            default:
                throw new UnsupportedValueType("Unrecognized value type name: "+name);
        }
    }
}
