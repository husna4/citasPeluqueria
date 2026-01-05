package com.saki.citasPeluqueria.configurations;


import com.saki.citasPeluqueria.exceptions.ObjectNotFoundException;
import com.saki.citasPeluqueria.util.MensajeUtil;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errores = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.add(error.getDefaultMessage()));

        return new ResponseEntity<>(createResponse(String.join("<br>", errores),
                HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleObjectNotFoundException(ObjectNotFoundException ex) {
        String mensaje = MensajeUtil.buildMensaje(messageSource, ObjectNotFoundException.ID_MENSAJE,
                ex.getNombreEntidad(), ex.getIdEntidad());

        return new ResponseEntity<>(createResponse(mensaje, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    private Map<String, Object> createResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("status", status.value());
        response.put("timestamp", getTimestampNow());
        return response;
    }

    private LocalDateTime getTimestampNow() {
        return LocalDateTime.now();
    }
}
