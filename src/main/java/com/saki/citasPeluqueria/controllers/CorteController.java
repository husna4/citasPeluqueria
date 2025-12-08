package com.saki.citasPeluqueria.controllers;


import com.saki.citasPeluqueria.dto.CorteDto;
import com.saki.citasPeluqueria.dto.CorteRequestDto;
import com.saki.citasPeluqueria.modelo.Corte;
import com.saki.citasPeluqueria.service.CorteService;
import com.saki.citasPeluqueria.util.ModelMapperUtil;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author husnain
 */

@RestController
@RequestMapping("/api/cortes")
public class CorteController {

    private final CorteService corteService;

    private final ModelMapper modelMapper;

    public CorteController(CorteService corteService, ModelMapper modelMapper) {
        this.corteService = corteService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<CorteDto>> obtenerCorte() {
        return ResponseEntity.ok(ModelMapperUtil.convertListEntityToDto(corteService.getCortes(),
                        CorteDto.class, modelMapper));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CorteDto> obtenerCorte(@PathVariable Long id) {
        return ResponseEntity.ok(modelMapper.map(corteService.getCorteById(id), CorteDto.class));
    }

    @PostMapping
    public ResponseEntity<CorteDto> crearNuevoCorte(@Valid @RequestBody CorteRequestDto corteDto) {
        Corte corte = corteService.crearCorte(corteDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(corte, CorteDto.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CorteDto> modificarCorte(@PathVariable Long id, @Valid @RequestBody CorteRequestDto corteDto) {
        Corte corte = corteService.modificarCorte(id, corteDto);

        if(corte == null) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(modelMapper.map(corte, CorteDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCorte(@PathVariable Long id) {
        corteService.eliminarCorte(id);

        return ResponseEntity.noContent().build();
    }
}
