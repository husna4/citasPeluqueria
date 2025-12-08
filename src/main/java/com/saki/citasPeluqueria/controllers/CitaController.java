package com.saki.citasPeluqueria.controllers;

import com.saki.citasPeluqueria.dto.AtenderCitaRequestDto;
import com.saki.citasPeluqueria.dto.CitaRequestDto;
import com.saki.citasPeluqueria.dto.CitaDto;
import com.saki.citasPeluqueria.mappers.CitaMapper;
import com.saki.citasPeluqueria.modelo.Cita;
import com.saki.citasPeluqueria.service.CitaService;
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
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;
    private final ModelMapper modelMapper;

    public CitaController(CitaService citaService, ModelMapper modelMapper) {
        this.citaService = citaService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<CitaDto>> obtenerCitas() {
        return ResponseEntity.ok(ModelMapperUtil.convertListEntityToDto(citaService.getCitas(),
                CitaDto.class, modelMapper));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaDto> obtenerCita(@PathVariable Long id) {
        return ResponseEntity.ok(modelMapper.map(citaService.getCitaById(id), CitaDto.class));
    }

    @GetMapping("/sin-atender")
    public ResponseEntity<List<CitaDto>> obtenerCitasSinAtender() {
        return ResponseEntity.ok(ModelMapperUtil.convertListEntityToDto(citaService.getCitasByAtendia(false),
                CitaDto.class, modelMapper));
    }

    @GetMapping("/atendidas")
    public ResponseEntity<List<CitaDto>> obtenerCitasAtendidas() {
        return ResponseEntity.ok(ModelMapperUtil.convertListEntityToDto(citaService.getCitasByAtendia(true),
                CitaDto.class, modelMapper));
    }

    @PostMapping
    public ResponseEntity<CitaDto> crearCita(@Valid @RequestBody CitaRequestDto citaDto) {
        Cita cita = citaService.crearCita(citaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(cita, CitaDto.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaDto> modificarCita(@PathVariable Long id,
                                                 @Valid @RequestBody CitaRequestDto citaDto) {

        Cita cita = citaService.modificarCita(id, citaDto);
        return ResponseEntity.ok(modelMapper.map(cita, CitaDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCita(@PathVariable Long id) {
        citaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/atender")
    public ResponseEntity<CitaDto> atenderCita(@PathVariable Long id,
                                               @Valid @RequestBody AtenderCitaRequestDto citaAtendidaDto) {
        Cita cita = citaService.atenderCita(id, citaAtendidaDto);

        return ResponseEntity.ok(modelMapper.map(cita, CitaDto.class));
    }
}
