package com.saki.citasPeluqueria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author husnain
 */
public class ClienteDto extends PersonaDto {
    @NotBlank(message = "{cliente.nombre.requerido}")
    @Size(max = 50, message = "{cliente.nombre.tamanyo}")
    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
