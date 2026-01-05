package com.saki.citasPeluqueria.dto;


import com.saki.citasPeluqueria.modelo.Cliente;
import com.saki.citasPeluqueria.modelo.ClienteAnonimo;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

/**
 * @author husnain
 */

@Getter
@Setter
public class CitaDto {
    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private boolean atendida;
    private BigDecimal precio;
    private String observaciones;
    private Set<CorteDto> cortes;
    private PeluqueroDto peluqueroAsignado;
    private ClienteDto cliente;
}
