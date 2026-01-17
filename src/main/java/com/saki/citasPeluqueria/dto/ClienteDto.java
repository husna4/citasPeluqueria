package com.saki.citasPeluqueria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
public class ClienteDto {
    private Long id;
    private String nombre;
    private String tfno;

}
