package com.saki.citasPeluqueria.modelo;

import com.saki.citasPeluqueria.interfaces.ICorte;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * @author husnain
 */
@Entity
public class Corte extends Identifiable implements ICorte {
    @Column(nullable = false, unique = true, length = 50)
    @NotBlank
    @Size(min = 1, max = 50)
    private String nombre;

    @Column(precision = 5, scale = 2)
    @NotNull
    private BigDecimal precio;

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public BigDecimal getPrecio() {
        return precio;
    }

    @Override
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
}
