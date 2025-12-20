package com.saki.citasPeluqueria.modelo;

import com.saki.citasPeluqueria.dataBuilder.CitaRequestDtoData;
import com.saki.citasPeluqueria.dto.CitaRequestDto;
import jakarta.validation.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author husnain
 */
public class CitaValidationTest {

    private final Validator validator;

    public CitaValidationTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    public static final String NOMBRE_PROPIEDAD_FECHA_CITA = "fecha";
    public static final String NOMBRE_PROPIEDAD_HORA_CITA = "hora";
    public static final String PROPIEDADA_ASSERTION_CITA_CLIENTE = "clienteValido";

    @Test
    @DisplayName("Debe fallar cuando la fecha es null")
    void testValidarFechaRequeridaAlCrearOModificarCita() {
        verificarValidacion(CitaRequestDtoData.SIN_FECHA,
                1, NOMBRE_PROPIEDAD_FECHA_CITA,
                NOMBRE_PROPIEDAD_FECHA_CITA);
    }

    @Test
    @DisplayName("Debe fallar cuando la hora es null")
    void testValidarHoraRequeridaAlCrearOModificarCita() {
        verificarValidacion(CitaRequestDtoData.SIN_HORA,
                1, NOMBRE_PROPIEDAD_HORA_CITA,
                NOMBRE_PROPIEDAD_HORA_CITA);
    }

    @Test
    @DisplayName("Debe fallar cuando existen varios errores de validación")
    void testValidarVariosCamposAlCrearOModificarCita() {
        String[] propiedadesConErrorEsperadas = {NOMBRE_PROPIEDAD_FECHA_CITA, NOMBRE_PROPIEDAD_HORA_CITA,
                PROPIEDADA_ASSERTION_CITA_CLIENTE};

        verificarValidacion(CitaRequestDtoData.SIN_FECHA_HORA_CLIENTE_CORTES, 3, propiedadesConErrorEsperadas);
    }

    @Test
    @DisplayName("No se debe crear la cita sin cliente")
    void testValidarCita_SinClienteAlCrearOModificarCita() {
        verificarValidacion(CitaRequestDtoData.SIN_CLIENTE, 1,
                PROPIEDADA_ASSERTION_CITA_CLIENTE);

    }

    @Test
    @DisplayName("Debe fallar la validación al existir tanto el id del cliente como el cliente anónimo")
    void testValidarCita_CuandoExisteIdClienteYClienteAnonimo_DebeDarError() {
        verificarValidacion(CitaRequestDtoData.CON_ID_CLIENTE_Y_CLIENTE_ANONIMO, 1,
                PROPIEDADA_ASSERTION_CITA_CLIENTE);
    }

    @Test
    @DisplayName("Se debe validar sin ningún error esperado")
    void testValidarSinErroresEsperadosAlCrearOModificarCita() {
        CitaRequestDto cita = CitaRequestDtoData.VALIDA_CON_ID_CLIENTE.getCitaDto();

        Set<ConstraintViolation<CitaRequestDto>> violations = validator.validate(cita);

        assertThat(violations).hasSize(0);
    }

    private void verificarValidacion(CitaRequestDtoData datosCitaDto,
                                     int numViolationsEsperadas,
                                     String... nombrePropiedadConErrorEsperado) {

        CitaRequestDto cita = datosCitaDto.getCitaDto();

        Set<ConstraintViolation<CitaRequestDto>> violations = validator.validate(cita);

        assertThat(violations).hasSize(numViolationsEsperadas);

        assertThat(violations)
                .hasSize(numViolationsEsperadas)
                .extracting(ConstraintViolation::getPropertyPath)
                .extracting(Path::toString)
                .contains(nombrePropiedadConErrorEsperado);
    }
}
