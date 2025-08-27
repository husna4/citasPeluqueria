package com.saki.citasPeluqueria.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * @author husnain
 */

@Converter(autoApply = false)
public class BooleanToIntegerConverter implements AttributeConverter<Boolean, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? 1 : 0;
    }

    @Override
    public Boolean convertToEntityAttribute(Integer dbData) {
        return dbData != null && dbData == 1;
    }
}
