package com.saki.citasPeluqueria.dto;

import jakarta.validation.constraints.Size;

import java.util.UUID;

public class PersonaDto {
    private UUID id;

    @Size(max = 16)
    private String tfno;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTfno() {
        return tfno;
    }

    public void setTfno(String tfno) {
        this.tfno = tfno;
    }
}
