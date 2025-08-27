package com.saki.citasPeluqueria.exceptions;

import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author husnain
 */
public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(MessageSource messageSource, String nombreEntidad, Object id) {
        super(messageSource.getMessage("objeto.no.encontrado",
                new Object[]{nombreEntidad, id}, Locale.getDefault()));
    }

    public ObjectNotFoundException(String mensaje) {
        super(mensaje);
    }
}
