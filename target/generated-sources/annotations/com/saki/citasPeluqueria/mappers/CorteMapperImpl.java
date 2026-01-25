package com.saki.citasPeluqueria.mappers;

import com.saki.citasPeluqueria.dto.CorteRequestDto;
import com.saki.citasPeluqueria.modelo.Corte;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-25T18:52:55+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Microsoft)"
)
@Component
public class CorteMapperImpl extends CorteMapper {

    @Override
    public Corte toEntity(CorteRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Corte corte = new Corte();

        corte.setNombre( dto.getNombre() );
        corte.setPrecio( dto.getPrecio() );
        corte.setDuracion( dto.getDuracion() );

        return corte;
    }

    @Override
    public Corte updateEntity(CorteRequestDto dto, Corte corte) {
        if ( dto == null ) {
            return corte;
        }

        corte.setNombre( dto.getNombre() );
        corte.setPrecio( dto.getPrecio() );
        corte.setDuracion( dto.getDuracion() );

        return corte;
    }
}
