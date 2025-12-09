package com.saki.citasPeluqueria.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author Husnain
 */
public final class MensajeUtil {

    private MensajeUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String buildMensaje(MessageSource messageSource, String idMensaje, Object... argumentos) {
        return messageSource.getMessage(idMensaje, argumentos, LocaleContextHolder.getLocale());
    }
}
