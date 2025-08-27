package com.saki.citasPeluqueria.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author husnain
 */
@MappedSuperclass
public abstract class Persona extends Identifiable {
    @Column(length = 50)
    @Size(min = 1, max = 50)
    @NotBlank
    protected String nombre;

    @Size(max = 16)
    @Column(length = 16)
    protected String tfno;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTfno() {
        return tfno;
    }

    public void setTfno(String tfno) {
        this.tfno = tfno;
    }
}
