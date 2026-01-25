package com.saki.citasPeluqueria.modelo;

import com.saki.citasPeluqueria.dataBuilder.CitaRequestDtoData;
import com.saki.citasPeluqueria.dto.CitaRequestDto;
import com.saki.citasPeluqueria.dto.ClienteDto;
import com.saki.citasPeluqueria.mappers.CitaMapperImpl;
import com.saki.citasPeluqueria.service.ClienteService;
import com.saki.citasPeluqueria.service.CorteService;
import com.saki.citasPeluqueria.service.PeluqueroService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author husnain
 */

@ExtendWith(MockitoExtension.class)
public class CitaMapperTest {
    @Mock
    private ClienteService clienteService;

    @Mock
    private CorteService corteService;

    @Mock
    private PeluqueroService peluqueroService;

    @InjectMocks
    private CitaMapperImpl citaMapper;

//    @Test
//    void toEntity_deberiaRecuperarClienteExistente_CuandoTieneIdCliente() {
//        Cliente clienteEsperado = crearCliente();
//        CitaRequestDto citaDto = CitaRequestDtoData.VALIDA_CON_ID_CLIENTE.getCitaDto();
//
//        when(clienteService.getClienteById(anyLong())).thenReturn(clienteEsperado);
//
//        Cita cita = citaMapper.toEntity(citaDto);
//
//        assertNotNull(cita.getCliente());
//        assertNull(cita.getClienteAnonimo());
//        assertEquals(clienteEsperado.getNombre(), cita.getCliente().getNombre());
//        assertEquals(clienteEsperado.getTfno(), cita.getCliente().getTfno());
//
//        verify(clienteService).getClienteById(anyLong());
//    }
//
//    @Test
//    void toEntity_deberiaCrearNuevoClienteAnonimo() {
//        CitaRequestDto citaDto = CitaRequestDtoData.CON_CLIENTE_ANONIMO.getCitaDto();
//        ClienteDto clienteAnonimoDto = citaDto.getClienteAnonimo();
//
//        Cita cita = citaMapper.toEntity(citaDto);
//
//        assertNotNull(cita.getClienteAnonimo());
//        assertNull(cita.getCliente());
//        assertEquals(clienteAnonimoDto.getNombre(), cita.getClienteAnonimo().getNombre());
//        assertEquals(clienteAnonimoDto.getTfno(), cita.getClienteAnonimo().getTfno());
//
//        verify(clienteService, never()).getClienteById(anyLong());
//    }

    private Cliente crearCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("David");
        cliente.setTfno("987654321");
        return cliente;
    }
}
