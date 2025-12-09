package com.saki.citasPeluqueria.exceptions;

import lombok.Getter;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author husnain
 */

@Getter
public class ObjectNotFoundException extends RuntimeException {

    private String nombreEntidad;
    private Object idEntidad;

    public static final String ID_MENSAJE = "objeto.no.encontrado";

    public ObjectNotFoundException(MessageSource messageSource, String nombreEntidad, Object id) {
        super(messageSource.getMessage("objeto.no.encontrado",
                new Object[]{nombreEntidad, id}, Locale.getDefault()));
    }

    public ObjectNotFoundException(String nombreEntidad, Object idEntidad) {
        this.nombreEntidad = nombreEntidad;
        this.idEntidad = idEntidad;
    }
}
