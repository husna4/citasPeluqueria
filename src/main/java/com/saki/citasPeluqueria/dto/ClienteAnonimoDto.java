package com.saki.citasPeluqueria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClienteAnonimoDto {

    @Size(max = 50, message = "{cliente.nombre.tamanyo}")
    private String nombre;

    @Size(max = 50, message = "cliente.tfno.tamanyo")
    private String tfno;

}
