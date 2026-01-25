package com.saki.citasPeluqueria.mappers;

import com.saki.citasPeluqueria.dto.CitaRequestDto;
import com.saki.citasPeluqueria.dto.ClienteDto;
import com.saki.citasPeluqueria.dto.ClienteRequestDto;
import com.saki.citasPeluqueria.modelo.*;
import com.saki.citasPeluqueria.service.ClienteService;
import com.saki.citasPeluqueria.service.CorteService;
import com.saki.citasPeluqueria.service.PeluqueroService;
import com.saki.citasPeluqueria.util.StringUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {ClienteService.class, PeluqueroService.class, CorteService.class})
public abstract class CitaMapper {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PeluqueroService peluqueroService;

    @Autowired
    private CorteService corteService;

    @Autowired
    private ModelMapper modelMapper;



    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cliente", expression = "java(estabelcerCliente(dto.getCliente()))")
    @Mapping(target = "peluqueroAsignado", expression = "java(findPeluqueroById(dto.getIdPeluqueroAsignado()))")
    @Mapping(target = "cortes", expression = "java(findCortesByIds(dto.getIdsCorte()))")
    public abstract Cita toEntity(CitaRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cliente", expression = "java(estabelcerCliente(dto.getCliente()))")
    @Mapping(target = "peluqueroAsignado", expression = "java(findPeluqueroById(dto.getIdPeluqueroAsignado()))")
    @Mapping(target = "cortes", expression = "java(findCortesByIds(dto.getIdsCorte()))")
    public abstract Cita updateEntity(CitaRequestDto dto, @MappingTarget Cita cita);

    protected Cliente estabelcerCliente(ClienteRequestDto clienteDto) {
        return clienteService.getByTfnoAndNombre(clienteDto.getTfno(), clienteDto.getNombre())
                .orElseGet(() -> clienteService.crearCliente(clienteDto));
    }

    protected Peluquero findPeluqueroById(Long id) {
        if (id == null) return null;
        return peluqueroService.getPeluqueroById(id);
    }

    protected Set<Corte> findCortesByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptySet();
        return new HashSet<>(corteService.getCorteByIds(ids));
    }
}