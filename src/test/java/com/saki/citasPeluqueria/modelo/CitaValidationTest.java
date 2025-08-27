package com.saki.citasPeluqueria.modelo;

import com.saki.citasPeluqueria.dataBuilder.CitaUpdateCreateDtoData;
import com.saki.citasPeluqueria.dto.CitaCreateUpdateDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CitaValidationTest {

    @Autowired
    private Validator validator;

    @Autowired
    private MessageSource messageSource;

    public static final String NOMBRE_PROPIEDAD_FECHA_CITA = "fecha";
    public static final String NOMBRE_PROPIEDAD_HORA_CITA = "hora";
    public static final String NOMBRE_PROPIEDAD_CLIENTE_CITA = "cliente";
    public static final String NOMBRE_PROPIEDAD_NOMBRE_CLIENTE = "cliente.nombre";
    public static final String NOMBRE_PROPIEDAD_IDS_CORTE = "idsCorte";

    @Test
    @DisplayName("Debe fallar cuando la fecha es null")
    void testValidarFechaRequeridaAlCrearOModificarCita() {
        verificarValidacion(CitaUpdateCreateDtoData.SIN_FECHA,
                1, NOMBRE_PROPIEDAD_FECHA_CITA,
                "cita.fecha.requerida");
    }

    @Test
    @DisplayName("Debe fallar cuando la hora es null")
    void testValidarHoraRequeridaAlCrearOModificarCita() {
        verificarValidacion(CitaUpdateCreateDtoData.SIN_HORA,
                1, NOMBRE_PROPIEDAD_HORA_CITA,
                "cita.hora.requerida");
    }

    @Test
    @DisplayName("Debe fallar cuando el cliente es null")
    void testValidarClienteRequeridaAlCrearOModificarCita() {
        verificarValidacion(CitaUpdateCreateDtoData.SIN_CLIENTE,
                1, NOMBRE_PROPIEDAD_CLIENTE_CITA,
                "cita.cliente.requerido");
    }

    @Test
    @DisplayName("Debe fallar cuando nombre del cliente en una cita es null")
    void testValidarNombreClienteRequeridaAlCrearOModificarCita() {
        verificarValidacion(CitaUpdateCreateDtoData.SIN_NOMBRE_CLIENTE,
                1,
                NOMBRE_PROPIEDAD_NOMBRE_CLIENTE,
                "cliente.nombre.requerido");
    }

    @Test
    @DisplayName("Debe fallar cuando la cita no tiene ningún id de corte")
    void testValidarIdCorteRequeridoAlCrearOModificarCita() {
        verificarValidacion(CitaUpdateCreateDtoData.SIN_CORTES,
                1,
                NOMBRE_PROPIEDAD_IDS_CORTE,
                "cita.cortes.requerido");
    }

    @Test
    @DisplayName("Debe fallar cuando existen varios errores de validación")
    void testValidarVariosCamposAlCrearOModificarCita() {

        CitaCreateUpdateDto cita = CitaUpdateCreateDtoData.SIN_FECHA_HORA_CLIENTE_CORTES.getCitaDto();

        int numViolationsEsperadas = 4;
        String[] propiedadesConErrorEsperadas = {NOMBRE_PROPIEDAD_FECHA_CITA, NOMBRE_PROPIEDAD_HORA_CITA,
        NOMBRE_PROPIEDAD_CLIENTE_CITA, NOMBRE_PROPIEDAD_IDS_CORTE};

        Set<ConstraintViolation<CitaCreateUpdateDto>> violations = validator.validate(cita);

        assertThat(violations)
                .hasSize(4)
                .extracting(ConstraintViolation::getPropertyPath)
                .extracting(Object::toString)
                .containsOnly(propiedadesConErrorEsperadas);
    }

    @Test
    @DisplayName("Se debe validar sin ningún error esperado")
    void testValidarSinErroresEsperadosAlCrearOModificarCita() {
        CitaCreateUpdateDto cita = CitaUpdateCreateDtoData.VALIDA.getCitaDto();

        Set<ConstraintViolation<CitaCreateUpdateDto>> violations = validator.validate(cita);

        assertThat(violations).hasSize(0);
    }

    private void verificarValidacion(@NotNull CitaUpdateCreateDtoData datosCitaDto,
                                     int numViolationsEsperadas,
                                     String nombrePropiedadConErrorEsperado,
                                     String mensajeErrorAlValidarEsperado) {

        CitaCreateUpdateDto cita = datosCitaDto.getCitaDto();

        Set<ConstraintViolation<CitaCreateUpdateDto>> violations = validator.validate(cita);

        assertThat(violations)
                .hasSize(numViolationsEsperadas)
                .extracting(ConstraintViolation::getPropertyPath)
                .extracting(Object::toString)
                .containsExactly(nombrePropiedadConErrorEsperado);

        String mensajeEsperado = messageSource.getMessage(
                mensajeErrorAlValidarEsperado,
                null,
                Locale.getDefault()
        );

        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .containsExactly(mensajeEsperado);

    }


}
