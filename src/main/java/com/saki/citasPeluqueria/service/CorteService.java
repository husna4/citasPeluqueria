package com.saki.citasPeluqueria.service;

import com.saki.citasPeluqueria.dto.CorteRequestDto;
import com.saki.citasPeluqueria.exceptions.ObjectNotFoundException;
import com.saki.citasPeluqueria.mappers.CorteMapper;
import com.saki.citasPeluqueria.modelo.Corte;
import com.saki.citasPeluqueria.repositorio.CorteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CorteService {

    private final CorteRepository corteRepository;
    private final CorteMapper corteMapper;

    public CorteService(CorteRepository corteRepository, CorteMapper corteMapper) {
        this.corteRepository = corteRepository;
        this.corteMapper = corteMapper;
    }
    public List<Corte> getCortes() {
        return corteRepository.findAll();
    }

    public Corte getCorteById(Long id) {
        return corteRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Corte.class.getSimpleName(), id));
    }

    public List<Corte> getCorteByIds(Collection<Long> ids) {
        return corteRepository.findAllById(ids);
    }

    @Transactional
    public Corte crearCorte(CorteRequestDto corteDto) {
        Corte corte = corteMapper.toEntity(corteDto);
        return corteRepository.save(corte);
    }

    @Transactional
    public Corte modificarCorte(Long id, CorteRequestDto corteDto) throws ObjectNotFoundException {
        Corte corte = getCorteById(id);
        corte = corteMapper.updateEntity(corteDto, corte);
        return corteRepository.save(corte);
    }

    @Transactional
    public void eliminarCorte(Long id) throws ObjectNotFoundException {
        getCorteById(id);
        corteRepository.deleteById(id);
    }
}
