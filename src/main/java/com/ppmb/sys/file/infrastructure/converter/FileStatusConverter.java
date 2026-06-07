package com.ppmb.sys.file.infrastructure.converter;

import com.ppmb.sys.file.domain.model.FileStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FileStatusConverter implements AttributeConverter<FileStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(FileStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public FileStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return FileStatus.fromValue(dbData);
    }
}
