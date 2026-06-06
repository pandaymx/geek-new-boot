package com.ppmb.user.infrastructure.persistence.converter;

import com.ppmb.user.domain.model.UserStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserStatusConverter implements AttributeConverter<UserStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public UserStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        for (UserStatus status : UserStatus.values()) {
            if (status.getCode() == dbData) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown database value for UserStatus: " + dbData);
    }
}
