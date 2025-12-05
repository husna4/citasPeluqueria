package com.saki.citasPeluqueria.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PersonaDto {
    private Long id;
    private String tfno;
}
