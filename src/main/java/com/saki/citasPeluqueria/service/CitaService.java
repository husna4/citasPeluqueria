package com.saki.citasPeluqueria.service;

import com.saki.citasPeluqueria.dto.AtenderCitaRequestDto;
import com.saki.citasPeluqueria.dto.CitaRequestDto;
import com.saki.citasPeluqueria.exceptions.ObjectNotFoundException;
import com.saki.citasPeluqueria.mappers.CitaMapper;
import com.saki.citasPeluqueria.modelo.Cita;
import com.saki.citasPeluqueria.repositorio.CitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author husnain
 */

@Service
@Transactional(readOnly = true)
public class CitaService {
    private final CitaRepository citaRepository;
    private final CitaMapper citaMapper;

    public CitaService(CitaRepository citaRepository, CitaMapper citaMapper) {
        this.citaRepository = citaRepository;
        this.citaMapper = citaMapper;
    }

    public List<Cita> getCitas() {
        return citaRepository.findAll();
    }

    public List<Cita> getCitasByAtendia(boolean atendida) {
        return citaRepository.findByAtendidaOrderByFechaAscHoraAsc(atendida);
    }

    public Cita getCitaById(Long id) {
        return citaRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Cita", id));
    }

    @Transactional
    public Cita crearCita(CitaRequestDto citaDto) {
        Cita cita = citaMapper.toEntity(citaDto);
        return citaRepository.save(cita);
    }

    @Transactional
    public Cita modificarCita(Long id, CitaRequestDto citaDto) {
        Cita cita = getCitaById(id);
        cita = citaMapper.updateEntity(citaDto, cita);
        return citaRepository.save(cita);
    }

    @Transactional
    public void eliminarCita(Long id) {
        citaRepository.deleteById(id);
    }

    @Transactional
    public Cita atenderCita(Long idCita, AtenderCitaRequestDto citaAtendidaDto) throws ObjectNotFoundException {
        Cita cita = getCitaById(idCita);

        cita.setAtendida(true);
        cita.setPrecio(citaAtendidaDto.getPrecio());

        return citaRepository.save(cita);
    }
}