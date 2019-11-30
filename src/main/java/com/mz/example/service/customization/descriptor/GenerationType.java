package com.mz.example.service.customization.descriptor;

import java.util.UUID;

@SuppressWarnings("unchecked")
public enum GenerationType {

    UNIQUE {
        @Override
        public <VALUE_TYPE> VALUE_TYPE generateValue(ValueTypeDescriptor<VALUE_TYPE> desc) {
            switch (desc.getValueType()){
                case NUMBER:
                    return (VALUE_TYPE) Long.valueOf(System.nanoTime());
                case STRING:
                    return (VALUE_TYPE) UUID.randomUUID().toString();
                default:
                    throw new UnsupportedOperationException("Unsupported value type: "+desc.getValueType());
            }
        }
    };

    public abstract <VALUE_TYPE> VALUE_TYPE generateValue(ValueTypeDescriptor<VALUE_TYPE> desc);
}
