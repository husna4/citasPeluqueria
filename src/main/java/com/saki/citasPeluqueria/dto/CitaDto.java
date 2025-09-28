package com.saki.citasPeluqueria.dto;


import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * @author husnain
 */

public class CitaDto extends CitaAbstractDto {
//    @NotNull(message = "{cita.cortes.requerido}")
    @Size(min = 1, message = "{cita.cortes.minimo}")
    private Set<CorteDto> cortes;

    private PeluqueroDto peluqueroAsignado;

    public Set<CorteDto> getCortes() {
        return cortes;
    }

    public void setCortes(Set<CorteDto> cortes) {
        this.cortes = cortes;
    }

    public PeluqueroDto getPeluqueroAsignado() {
        return peluqueroAsignado;
    }

    public void setPeluqueroAsignado(PeluqueroDto peluqueroAsignado) {
        this.peluqueroAsignado = peluqueroAsignado;
    }
}
