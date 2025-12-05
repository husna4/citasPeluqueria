package com.saki.citasPeluqueria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author husnain
 */

@Getter
@Setter
public class CorteDto {
    private UUID id;

    @NotBlank(message = "{corte.error.nombre.requerido}")
    @Size(max = 50)
    private String nombre;

    @NotNull(message = "{corte.error.precio.requerido}")
    private BigDecimal precio;

    private int duracion = 0; // en minutos
}
