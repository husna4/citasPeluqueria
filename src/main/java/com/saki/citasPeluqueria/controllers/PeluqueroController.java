package com.saki.citasPeluqueria.controllers;

import com.saki.citasPeluqueria.dto.PeluqueroDto;
import com.saki.citasPeluqueria.modelo.Peluquero;
import com.saki.citasPeluqueria.service.PeluqueroService;
import com.saki.citasPeluqueria.util.Util;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/peluquero")
public class PeluqueroController {

    @Autowired
    private PeluqueroService peluqueroService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<PeluqueroDto>> obtenerPeluqueros() {
        return ResponseEntity.ok(Util.convertListEntityToDto(peluqueroService.getAllPeluqueros(),
                PeluqueroDto.class, modelMapper));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeluqueroDto> obtenerPeluquero(@PathVariable UUID id) {
        return peluqueroService.getPeluqueroById(id)
                .map(p -> ResponseEntity.ok(modelMapper.map(p, PeluqueroDto.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PeluqueroDto> crearPeluquero(@Valid @RequestBody PeluqueroDto dto) {
        Peluquero p = peluqueroService.crearPeluquero(dto);

        return p == null ? ResponseEntity.badRequest().build() :
                ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(p, PeluqueroDto.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeluqueroDto> modificarPeluquero(@PathVariable UUID id, @Valid @RequestBody PeluqueroDto dto) {
        Peluquero peluquero = peluqueroService.modificarPeluquero(id, dto);

        if (peluquero == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(modelMapper.map(peluquero, PeluqueroDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PeluqueroDto> eliminarPeluquero(@PathVariable UUID id) {
        peluqueroService.eliminarPeluquero(id);

        return ResponseEntity.noContent().build();
    }
}
