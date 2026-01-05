package com.saki.citasPeluqueria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClienteDto {

    @NotBlank(message = "{cliente.nombre.requerido}")
    @Size(max = 50, message = "{cliente.nombre.tamanyo}")
    private String nombre;

    @Size(max = 50, message = "cliente.tfno.tamanyo")
    private String tfno;

}
