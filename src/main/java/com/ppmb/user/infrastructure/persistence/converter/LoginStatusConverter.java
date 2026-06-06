package com.ppmb.user.infrastructure.persistence.converter;

import com.ppmb.user.domain.model.LoginStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LoginStatusConverter implements AttributeConverter<LoginStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(LoginStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public LoginStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        for (LoginStatus status : LoginStatus.values()) {
            if (status.getCode() == dbData) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown database value for LoginStatus: " + dbData);
    }
}
