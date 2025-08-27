package com.saki.citasPeluqueria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author husnain
 */
public class PeluqueroDto extends PersonaDto {
    @NotBlank(message = "{peluquero.nombre.vacio}")
    @Size(max = 50, message = "{peluquero.nombre.tamanyo}")
    private String nombre;

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}