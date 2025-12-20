package com.saki.citasPeluqueria.mappers;

import com.saki.citasPeluqueria.dto.CitaRequestDto;
import com.saki.citasPeluqueria.dto.ClienteAnonimoDto;
import com.saki.citasPeluqueria.modelo.*;
import com.saki.citasPeluqueria.service.ClienteService;
import com.saki.citasPeluqueria.service.CorteService;
import com.saki.citasPeluqueria.service.PeluqueroService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class CitaMapper {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PeluqueroService peluqueroService;

    @Autowired
    private CorteService corteService;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cliente", expression = "java(findClienteById(dto.getIdCliente()))")
    @Mapping(target = "peluqueroAsignado", expression = "java(findPeluqueroById(dto.getIdPeluqueroAsignado()))")
    @Mapping(target = "cortes", expression = "java(findCortesByIds(dto.getIdsCorte()))")
    public abstract Cita toEntity(CitaRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cliente", expression = "java(findClienteById(dto.getIdCliente()))")
    @Mapping(target = "peluqueroAsignado", expression = "java(findPeluqueroById(dto.getIdPeluqueroAsignado()))")
    @Mapping(target = "cortes", expression = "java(findCortesByIds(dto.getIdsCorte()))")
    public abstract Cita updateEntity(CitaRequestDto dto, @MappingTarget Cita cita);

    public ClienteAnonimo toClienteAnonimo(ClienteAnonimoDto dto) {
        if (dto == null) return null;

        ClienteAnonimo clienteAnonimo = new ClienteAnonimo();
        clienteAnonimo.setNombre(dto.getNombre());
        clienteAnonimo.setTfno(dto.getTfno());
        return clienteAnonimo;
    }

    protected Cliente findClienteById(Long id) {
        if (id == null) return null;
        return clienteService.getClienteById(id);
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