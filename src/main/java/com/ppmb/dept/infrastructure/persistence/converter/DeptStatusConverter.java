package com.ppmb.dept.infrastructure.persistence.converter;

import com.ppmb.dept.domain.model.DeptStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DeptStatusConverter implements AttributeConverter<DeptStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DeptStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public DeptStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        for (DeptStatus status : DeptStatus.values()) {
            if (status.getCode() == dbData) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown database value for DeptStatus: " + dbData);
    }
}
