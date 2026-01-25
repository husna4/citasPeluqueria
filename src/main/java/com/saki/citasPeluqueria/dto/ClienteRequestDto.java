package com.saki.citasPeluqueria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

/**
 * @author husnain
 */
@Data
@Builder
public class ClienteRequestDto {

    @NotBlank(message = "{cliente.nombre.requerido}")
    @Size(max = 50, message = "{cliente.nombre.tamanyo}")
    private String nombre;

    @Size(max = 50, message = "cliente.tfno.tamanyo")
    @Pattern(
            regexp = "^[0-9 ]*$",
            message = "{cliente.tfno.formato}"
    )
    private String tfno;

}
