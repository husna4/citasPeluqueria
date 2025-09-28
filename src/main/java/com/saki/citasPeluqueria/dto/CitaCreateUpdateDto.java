package com.saki.citasPeluqueria.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author husnain
 */
public class CitaCreateUpdateDto extends CitaAbstractDto {

//    @NotEmpty(message = "{cita.cortes.requerido}")
    private Set<UUID> idsCorte;

    private UUID idPeluqueroAsignado;

    public Set<UUID> getIdsCorte() {
        return idsCorte;
    }

    public void setIdsCorte(Set<UUID> idsCorte) {
        this.idsCorte = idsCorte;
    }

    public UUID getIdPeluqueroAsignado() {
        return idPeluqueroAsignado;
    }

    public void setIdPeluqueroAsignado(UUID idPeluqueroAsignado) {
        this.idPeluqueroAsignado = idPeluqueroAsignado;
    }

    public void addIdCorte(UUID idCorte) {
        if(getIdsCorte() == null) {
            setIdsCorte(new HashSet<>());
        }

        getIdsCorte().add(idCorte);
    }
}
