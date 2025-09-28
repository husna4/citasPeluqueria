package com.saki.citasPeluqueria.controllers;

import com.saki.citasPeluqueria.dto.CitaCreateUpdateDto;
import com.saki.citasPeluqueria.dto.CitaDto;
import com.saki.citasPeluqueria.modelo.Cita;
import com.saki.citasPeluqueria.service.CitaService;
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
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<CitaDto>> obtenerCitas() {
        List<CitaDto> citasDto = citaService.getCitas().stream()
                .map(cita -> modelMapper.map(cita, CitaDto.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(citasDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaDto> obtenerCita(@PathVariable UUID id) {

        return citaService.getCitaById(id)
                .map(c -> ResponseEntity.ok(modelMapper.map(c, CitaDto.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sin-atender")
    public ResponseEntity<List<CitaDto>> obtenerCitasSinAtender() {

        List<CitaDto> citasDto = citaService.getCitasSinAtender().stream()
                .map(cita -> modelMapper.map(cita, CitaDto.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(citasDto);
    }

    @PostMapping
    public ResponseEntity<CitaDto> crearCita(@Valid @RequestBody CitaCreateUpdateDto citaDto) {
         Cita cita = citaService.crearCita(citaDto);

        if(cita == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(cita, CitaDto.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaDto> modificarCita(@PathVariable UUID id,
                                                 @Valid @RequestBody CitaCreateUpdateDto citaDto) {

        Cita cita = citaService.modificarCita(id, citaDto);

        if(cita == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(modelMapper.map(cita, CitaDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCita(@PathVariable UUID id) {
        citaService.eliminarCita(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/atender")
    public ResponseEntity<CitaDto> atenderCita(@PathVariable UUID id) {
        Cita cita = citaService.atenderCita(id);

        if(cita == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(modelMapper.map(cita, CitaDto.class));
    }
}
