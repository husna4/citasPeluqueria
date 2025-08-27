package com.saki.citasPeluqueria.modelo;

import com.saki.citasPeluqueria.interfaces.IIdenifiable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.UUID;

/**
 * @author Husnain
 */


@MappedSuperclass
public abstract class Identifiable implements IIdenifiable {
    @GeneratedValue
    @Id
    private UUID id;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }
}
