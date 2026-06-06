package com.ppmb.user.infrastructure.persistence.converter;

import com.ppmb.user.domain.model.IdentityType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class IdentityTypeConverter implements AttributeConverter<IdentityType, String> {

    @Override
    public String convertToDatabaseColumn(IdentityType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public IdentityType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        for (IdentityType type : IdentityType.values()) {
            if (type.getCode().equals(dbData)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown database value for IdentityType: " + dbData);
    }
}
