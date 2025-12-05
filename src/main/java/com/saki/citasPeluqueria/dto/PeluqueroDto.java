package com.saki.citasPeluqueria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author husnain
 */

@Getter
@Setter
public class PeluqueroDto extends PersonaDto {
    @NotBlank(message = "{peluquero.nombre.vacio}")
    @Size(max = 50, message = "{peluquero.nombre.tamanyo}")
    private String nombre;
}