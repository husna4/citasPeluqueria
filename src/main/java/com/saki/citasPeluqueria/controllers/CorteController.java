package com.saki.citasPeluqueria.controllers;


import com.saki.citasPeluqueria.dto.CorteDto;
import com.saki.citasPeluqueria.modelo.Corte;
import com.saki.citasPeluqueria.service.CorteService;
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

    @Autowired
    private CorteService corteService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<CorteDto>> obtenerCorte() {
        return ResponseEntity.ok(
                corteService.getCortes().stream().map(corte -> modelMapper.map(corte, CorteDto.class))
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CorteDto> obtenerCorte(@PathVariable UUID id) {
        return corteService.getCorteById(id).map(c ->
                ResponseEntity.ok(modelMapper.map(c, CorteDto.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CorteDto> crearNuevoCorte(@Valid @RequestBody CorteDto corteDto) {
        Corte corte = corteService.crearCorte(corteDto);

        if(corte == null) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(corte, CorteDto.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CorteDto> modificarCorte(@PathVariable UUID id, @Valid @RequestBody CorteDto corteDto) {
        Corte corte = corteService.modificarCorte(id, corteDto);

        if(corte == null) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(modelMapper.map(corte, CorteDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCorte(@PathVariable UUID id) {
        corteService.eliminarCorte(id);

        return ResponseEntity.noContent().build();
    }
}
