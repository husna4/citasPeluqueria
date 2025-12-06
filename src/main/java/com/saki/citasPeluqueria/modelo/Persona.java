package com.saki.citasPeluqueria.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author husnain
 */
@MappedSuperclass
@Getter
@Setter
public abstract class Persona extends Identifiable {
    @Column(length = 50)
    protected String nombre;

    @Column(length = 16)
    protected String tfno;
}
