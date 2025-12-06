package com.saki.citasPeluqueria.modelo;

import com.saki.citasPeluqueria.converters.BooleanToIntegerConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

/**
 * @author husnain
 */

@Getter
@Setter
@Entity
@Table
public class Cita extends Identifiable {
    @NotNull
    private LocalDate fecha;

    @NotNull
    private LocalTime hora;

    @ColumnDefault("0")
    @Convert(converter = BooleanToIntegerConverter.class)
    private boolean atendida;

    @ManyToMany
    @JoinTable(name = "cita_corte",
        joinColumns = @JoinColumn(name = "idCita"),
        inverseJoinColumns = @JoinColumn(name = "idCorte"))
    private Set<Corte> cortes;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "idCliente", referencedColumnName = "id")
    private Cliente cliente;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idClienteAnonimo", referencedColumnName = "id")
    private ClienteAnonimo clienteAnonimo;

    @ManyToOne
    @JoinColumn(name = "idPeluquero", referencedColumnName = "id")
    private Peluquero peluqueroAsignado;

    @Column(precision = 5, scale = 2)
    private BigDecimal precio;

    @Column(length = 1000)
    private String observaciones;
}
