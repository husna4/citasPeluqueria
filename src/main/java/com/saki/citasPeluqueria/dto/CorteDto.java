package com.saki.citasPeluqueria.dto;

import jakarta.validation.constraints.Digits;
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
    private Long id;

    @NotBlank(message = "{corte.error.nombre.requerido}")
    @Size(max = 50, message = "{corte.nombre.tamanyo}")
    private String nombre;

    @NotNull(message = "{corte.error.precio.requerido}")
    @Digits(integer = 10, fraction = 2, message = "{corte.error.precio.formato}")
    private BigDecimal precio;

    private int duracion = 0; // en minutos
}
