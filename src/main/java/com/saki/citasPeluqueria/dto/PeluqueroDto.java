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
public class PeluqueroDto {
    private Long id;

    private String tfno;

    private String nombre;
}