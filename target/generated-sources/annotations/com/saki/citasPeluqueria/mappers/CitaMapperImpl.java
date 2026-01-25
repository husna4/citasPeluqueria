package com.saki.citasPeluqueria.mappers;

import com.saki.citasPeluqueria.dto.CitaRequestDto;
import com.saki.citasPeluqueria.modelo.Cita;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-25T18:52:55+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Microsoft)"
)
@Component
public class CitaMapperImpl extends CitaMapper {

    @Override
    public Cita toEntity(CitaRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Cita cita = new Cita();

        cita.setFecha( dto.getFecha() );
        cita.setHora( dto.getHora() );
        cita.setAtendida( dto.isAtendida() );
        cita.setPrecio( dto.getPrecio() );
        cita.setObservaciones( dto.getObservaciones() );

        cita.setCliente( estabelcerCliente(dto.getCliente()) );
        cita.setPeluqueroAsignado( findPeluqueroById(dto.getIdPeluqueroAsignado()) );
        cita.setCortes( findCortesByIds(dto.getIdsCorte()) );

        return cita;
    }

    @Override
    public Cita updateEntity(CitaRequestDto dto, Cita cita) {
        if ( dto == null ) {
            return cita;
        }

        cita.setFecha( dto.getFecha() );
        cita.setHora( dto.getHora() );
        cita.setAtendida( dto.isAtendida() );
        cita.setPrecio( dto.getPrecio() );
        cita.setObservaciones( dto.getObservaciones() );

        cita.setCliente( estabelcerCliente(dto.getCliente()) );
        cita.setPeluqueroAsignado( findPeluqueroById(dto.getIdPeluqueroAsignado()) );
        cita.setCortes( findCortesByIds(dto.getIdsCorte()) );

        return cita;
    }
}
