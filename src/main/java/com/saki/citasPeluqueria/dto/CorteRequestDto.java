package com.saki.citasPeluqueria.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author husnain
 */

@Data
public class CorteRequestDto {
    @NotBlank(message = "{corte.error.nombre.requerido}")
    @Size(max = 50, message = "{corte.nombre.tamanyo}")
    private String nombre;

    @NotNull(message = "{corte.error.precio.requerido}")
    @Digits(integer = 10, fraction = 2, message = "{corte.error.precio.formato}")
    private BigDecimal precio;

    private int duracion = 0; // en minutos
}
