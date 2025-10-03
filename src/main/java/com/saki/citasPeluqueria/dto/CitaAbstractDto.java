package com.saki.citasPeluqueria.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * @author husnain
 */
public abstract class CitaAbstractDto {

    private UUID id;

    @NotNull(message = "{cita.fecha.requerida}")
    private LocalDate fecha;

    @NotNull(message = "{cita.hora.requerida}")
    private LocalTime hora;

    private boolean atendida;

//    @NotNull(message = "{cita.cliente.requerido}")
//    @Valid
    private ClienteDto cliente;

    @Length(max = 1000)
    private String observaciones;

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public boolean isAtendida() {
        return atendida;
    }

    public void setAtendida(boolean atendida) {
        this.atendida = atendida;
    }

    public ClienteDto getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDto cliente) {
        this.cliente = cliente;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
