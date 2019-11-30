package com.mz.example.service.customization.descriptor;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mz.example.jackson.databind.StringToValueTypeDescriptorConverter;
import com.mz.example.jackson.databind.ValueTypeDescriptorConverterToString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
//Thymeleaf requires setters to populate fields with form values
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class CustomizationDescriptor<VALUE_TYPE> {

    @JsonDeserialize(converter = StringToValueTypeDescriptorConverter.class)
    @JsonSerialize(converter = ValueTypeDescriptorConverterToString.class)
    private ValueTypeDescriptor<VALUE_TYPE> valueType;
    private GenerationType generationType;
    private String valuePath;

    public VALUE_TYPE generateValue(){
        return getGenerationType().generateValue(getValueType());
    }
}
