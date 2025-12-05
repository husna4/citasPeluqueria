package com.saki.citasPeluqueria.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author husnain
 */
@Entity
@Getter
@Setter
public class Corte extends Identifiable {
    @Column(nullable = false, unique = true, length = 50)
    @NotBlank
    @Size(min = 1, max = 50)
    private String nombre;

    @Column(precision = 5, scale = 2)
    @NotNull
    private BigDecimal precio;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private int duracion = 0; // en minutos
}
