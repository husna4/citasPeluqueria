package com.saki.citasPeluqueria.service;


import com.saki.citasPeluqueria.dto.PeluqueroDto;
import com.saki.citasPeluqueria.exceptions.ObjectNotFoundException;
import com.saki.citasPeluqueria.modelo.Peluquero;
import com.saki.citasPeluqueria.repositorio.PeluqueroRepository;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author husnain
 */

@Service
public class PeluqueroService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PeluqueroRepository peluqueroRepository;

    public List<Peluquero> getAllPeluqueros(){
        return peluqueroRepository.findAll();
    }

    public Optional<Peluquero> getPeluqueroById(UUID id) {
        if(id == null) {
            return Optional.empty();
        }

        return peluqueroRepository.findById(id);
    }

    public Peluquero crearPeluquero(PeluqueroDto peluqueroDto) {
        Peluquero peluquero = modelMapper.map(peluqueroDto, Peluquero.class);

        return peluqueroRepository.save(peluquero);
    }

    public Peluquero modificarPeluquero(@NotNull UUID id,
                                        @NotNull PeluqueroDto peluqueroDto) throws ObjectNotFoundException {

        Peluquero peluquero = getPeluqueroById(id).orElseThrow(() ->
                new ObjectNotFoundException(messageSource, Peluquero.class.getSimpleName(), id));

        peluquero.setNombre(peluqueroDto.getNombre());
        peluquero.setTfno(peluqueroDto.getTfno());

        return peluqueroRepository.save(peluquero);
    }

    public void eliminarPeluquero(@NotNull UUID id) throws ObjectNotFoundException {
        getPeluqueroById(id).orElseThrow(() ->
                new ObjectNotFoundException(messageSource, Peluquero.class.getSimpleName(), id));

        peluqueroRepository.deleteById(id);
    }
}
