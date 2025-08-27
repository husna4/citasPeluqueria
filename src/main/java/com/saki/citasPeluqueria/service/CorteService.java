package com.saki.citasPeluqueria.service;


import com.saki.citasPeluqueria.dto.CorteDto;
import com.saki.citasPeluqueria.exceptions.ObjectNotFoundException;
import com.saki.citasPeluqueria.modelo.Corte;
import com.saki.citasPeluqueria.repositorio.CorteRepository;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CorteService {

    @Autowired
    private CorteRepository corteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MessageSource messageSource;

    public List<Corte> getCortes() {
        return corteRepository.findAll();
    }

    public Optional<Corte> getCorteById(UUID id) {
        return corteRepository.findById(id);
    }

    public List<Corte> getCorteByIds(List<UUID> ids) {
        return corteRepository.findAllById(ids);
    }

    public Corte crearCorte(@NotNull CorteDto corteDto) {
        Corte corte = modelMapper.map(corteDto, Corte.class);
        return corteRepository.save(corte);
    }

    public Corte modificarCorte(@NotNull UUID id, @NotNull CorteDto corteDto) throws ObjectNotFoundException {
        Corte corte = getCorteById(id).orElseThrow(
                () -> new ObjectNotFoundException(messageSource, Corte.class.getSimpleName(), id));

        corte.setNombre(corteDto.getNombre());
        corte.setPrecio(corteDto.getPrecio());

        return corteRepository.save(corte);
    }

    public void eliminarCorte(@NotNull UUID id) throws ObjectNotFoundException {
        getCorteById(id).orElseThrow(
                () -> new ObjectNotFoundException(messageSource, Corte.class.getSimpleName(), id));

        corteRepository.deleteById(id);
    }
}
