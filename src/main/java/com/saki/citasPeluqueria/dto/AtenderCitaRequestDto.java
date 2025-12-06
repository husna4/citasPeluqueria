package com.saki.citasPeluqueria.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author husnain
 */

@Data
public class AtenderCitaRequestDto {
    @NotNull(message = "{cita.precio.requerido}")
    @DecimalMin(value = "0.0", inclusive = true, message = "{cita.precio.positivo}")
    @Digits(integer = 10, fraction = 2, message = "{cita.precio.formato}")
    private BigDecimal precio;
}
