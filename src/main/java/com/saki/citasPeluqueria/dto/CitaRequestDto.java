package com.saki.citasPeluqueria.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author husnain
 */

@Data
@Builder
public class CitaRequestDto {

//    @NotEmpty(message = "{cita.cortes.requerido}")
    private Set<UUID> idsCorte;

    private UUID idPeluqueroAsignado;

    @NotNull(message = "{cita.fecha.requerida}")
    private LocalDate fecha;

    @NotNull(message = "{cita.hora.requerida}")
    private LocalTime hora;

    private boolean atendida;

    @Digits(integer = 10, fraction = 2, message = "{cita.precio.formato}")
    private BigDecimal precio;

    @Length(max = 1000)
    private String observaciones;

    @Size(max = 50, message = "{cliente.nombre.tamanyo}")
    private String nombreCliente;


    private Long idCliente;

    public void addIdCorte(UUID idCorte) {
        if(getIdsCorte() == null) {
            setIdsCorte(new HashSet<>());
        }

        getIdsCorte().add(idCorte);
    }
}
