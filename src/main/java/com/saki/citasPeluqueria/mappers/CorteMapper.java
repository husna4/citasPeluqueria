package com.saki.citasPeluqueria.mappers;

import com.saki.citasPeluqueria.dto.CorteRequestDto;
import com.saki.citasPeluqueria.modelo.Corte;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author husnain
 */
@Mapper(componentModel = "spring")
public abstract class CorteMapper {

    @Mapping(target = "id", ignore = true)
    public abstract Corte toEntity(CorteRequestDto dto);

    @Mapping(target = "id", ignore = true)
    public abstract Corte updateEntity(CorteRequestDto dto, @MappingTarget Corte corte);
}
