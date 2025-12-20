package com.saki.citasPeluqueria.dataBuilder;

import com.saki.citasPeluqueria.dto.CitaRequestDto;
import com.saki.citasPeluqueria.dto.ClienteAnonimoDto;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;


/**
 * @author husnain
 */

@Getter
public enum CitaRequestDtoData {

    SIN_FECHA(CitaRequestDto.builder()
            .hora(LocalTime.of(10, 0))
            .clienteAnonimo(ClienteAnonimoDto.builder().nombre("Junit1").build())
            .build()),

    SIN_HORA(CitaRequestDto.builder()
            .fecha(LocalDate.of(2021, 1, 1))
            .clienteAnonimo(ClienteAnonimoDto.builder().nombre("Junit1").build())
            .build()),

    SIN_CLIENTE(CitaRequestDto.builder()
            .fecha(LocalDate.of(2021, 1, 1))
            .hora(LocalTime.of(10, 0))
            .build()),

    SIN_FECHA_HORA_CLIENTE_CORTES(CitaRequestDto.builder()
            .build()),

    VALIDA_CON_ID_CLIENTE(CitaRequestDto.builder()
            .fecha(LocalDate.of(2021,1,1))
            .hora(LocalTime.of(11,0))
            .idCliente(12344L)
            .build()),

    CON_CLIENTE_ANONIMO(CitaRequestDto.builder()
            .fecha(LocalDate.of(2021,1,1))
            .hora(LocalTime.of(11,0))
            .clienteAnonimo(ClienteAnonimoDto.builder().nombre("Cliente_JUnit1").tfno("221122336L").build())
            .build()),

    CON_ID_CLIENTE_Y_CLIENTE_ANONIMO(CitaRequestDto.builder()
            .fecha(LocalDate.of(2021,1,1))
            .hora(LocalTime.of(11,0))
            .idCliente(12354L)
            .clienteAnonimo(ClienteAnonimoDto.builder().nombre("Cliente_JUnit2").tfno("221422336L").build())
            .build())
    ;

    private final CitaRequestDto citaDto;

    CitaRequestDtoData(CitaRequestDto citaDto) {
        this.citaDto = citaDto;
    }
}
