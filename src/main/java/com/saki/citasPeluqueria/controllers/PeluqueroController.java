package com.saki.citasPeluqueria.controllers;

import com.saki.citasPeluqueria.dto.PeluqueroDto;
import com.saki.citasPeluqueria.modelo.Peluquero;
import com.saki.citasPeluqueria.service.PeluqueroService;
import com.saki.citasPeluqueria.util.ModelMapperUtil;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/peluquero")
public class PeluqueroController {

    @Autowired
    private PeluqueroService peluqueroService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<PeluqueroDto>> obtenerPeluqueros() {
        return ResponseEntity.ok(ModelMapperUtil.convertListEntityToDto(peluqueroService.getAllPeluqueros(),
                PeluqueroDto.class, modelMapper));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeluqueroDto> obtenerPeluquero(@PathVariable Long id) {
        return ResponseEntity.ok(modelMapper.map(peluqueroService.getPeluqueroById(id), PeluqueroDto.class));
    }

    @PostMapping
    public ResponseEntity<PeluqueroDto> crearPeluquero(@Valid @RequestBody PeluqueroDto dto) {
        Peluquero p = peluqueroService.crearPeluquero(dto);

        return p == null ? ResponseEntity.badRequest().build() :
                ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(p, PeluqueroDto.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeluqueroDto> modificarPeluquero(@PathVariable Long id, @Valid @RequestBody PeluqueroDto dto) {
        Peluquero peluquero = peluqueroService.modificarPeluquero(id, dto);

        if (peluquero == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(modelMapper.map(peluquero, PeluqueroDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PeluqueroDto> eliminarPeluquero(@PathVariable Long id) {
        peluqueroService.eliminarPeluquero(id);

        return ResponseEntity.noContent().build();
    }
}
