package com.saki.citasPeluqueria.modelo;

import com.saki.citasPeluqueria.converters.BooleanToIntegerConverter;
import com.saki.citasPeluqueria.interfaces.ICita;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

/**
 * @author husnain
 */
@Entity
@Table
public class Cita extends Identifiable implements ICita {
    @NotNull
    private LocalDate fecha;

    @NotNull
    private LocalTime hora;

    @ColumnDefault("0")
    @Convert(converter = BooleanToIntegerConverter.class)
    private boolean atendida;

    @NotEmpty
    @ManyToMany
    @JoinTable(name = "cita_corte",
        joinColumns = @JoinColumn(name = "idCita"),
        inverseJoinColumns = @JoinColumn(name = "idCorte"))
    private Set<Corte> cortes;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "idCliente", referencedColumnName = "id")
    @NotNull
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "idPeluquero", referencedColumnName = "id")
    private Peluquero peluqueriAsignado;

    @Override
    public LocalDate getFecha() {
        return fecha;
    }

    @Override
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public LocalTime getHora() {
        return hora;
    }

    @Override
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    @Override
    public Set<Corte> getCortes() {
        return cortes;
    }

    @Override
    public void setCortes(Set<Corte> cortes) {
        this.cortes = cortes;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Peluquero getPeluqueriAsignado() {
        return peluqueriAsignado;
    }

    public void setPeluqueriAsignado(Peluquero peluqueriAsignado) {
        this.peluqueriAsignado = peluqueriAsignado;
    }

    public boolean isAtendida() {
        return atendida;
    }

    public void setAtendida(boolean atendida) {
        this.atendida = atendida;
    }
}
