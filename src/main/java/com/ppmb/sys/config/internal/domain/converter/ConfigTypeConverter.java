package com.ppmb.sys.config.internal.domain.converter;

import com.ppmb.sys.config.ConfigType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ConfigTypeConverter implements AttributeConverter<ConfigType, String> {

    @Override
    public String convertToDatabaseColumn(ConfigType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public ConfigType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return ConfigType.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
