package com.saki.citasPeluqueria.modelo;

import com.saki.citasPeluqueria.dataBuilder.CitaUpdateCreateDtoData;
import com.saki.citasPeluqueria.dataBuilder.ClienteDtoData;
import com.saki.citasPeluqueria.dataBuilder.CorteDtoData;
import com.saki.citasPeluqueria.dto.AtenderCitaRequestDto;
import com.saki.citasPeluqueria.dto.CitaCreateUpdateDto;
import com.saki.citasPeluqueria.dto.ClienteDto;
import com.saki.citasPeluqueria.repositorio.CitaRepository;
import com.saki.citasPeluqueria.service.CitaService;
import com.saki.citasPeluqueria.service.ClienteService;
import com.saki.citasPeluqueria.service.CorteService;
import com.saki.citasPeluqueria.service.PeluqueroService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

/**
 * @author husnain
 */

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CitaServiceTest {
    @Mock
    private ModelMapper modelMapperMock;

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private CorteService corteService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private PeluqueroService peluqueroService;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private CitaService citaService;

    @Test
    @DisplayName("Crear cita creando nuevo cliente")
    void crearCita_ClienteNoExistente_DeberiaCrearNuevoCliente() {
        CitaCreateUpdateDto citaDto = CitaUpdateCreateDtoData.VALIDA_CON_PELUQUERO_SIN_ID_CLIENTE.getCitaDto();
        Cita cita = CitaUpdateCreateDtoData.VALIDA_CON_PELUQUERO_SIN_ID_CLIENTE.getCita();

        when(modelMapperMock.map(citaDto, Cita.class)).thenReturn(cita);
        when(clienteService.getClienteById(cita.getCliente().getId())).thenReturn(Optional.empty()); // debería crear cliente nuevo
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        Cita resultado = citaService.crearCita(citaDto);

        assertNotNull(resultado);
        assertEquals(cita.getId(), resultado.getId());

        verify(clienteService).getClienteById(citaDto.getCliente().getId());
        verify(clienteService).crearClienteDesdeDto(citaDto.getCliente());
        verify(citaRepository).save(any(Cita.class));
    }

    @Test
    @DisplayName("Crear nueva cita obteniendo cliente de la BD")
    void crearCita_ClienteExistente_NoDeberiaCrearNuevoCliente() {
        CitaCreateUpdateDto citaDto = CitaUpdateCreateDtoData
                .VALIDA_CON_PELUQUERO_CON_ID_CLIENTE.getCitaDto();

        Cita citaEsperada = CitaUpdateCreateDtoData
                .VALIDA_CON_PELUQUERO_CON_ID_CLIENTE.getCita();

        Cliente clienteExistente = ClienteDtoData.VALIDO_JUAN_CON_ID.getCliente();

        when(modelMapperMock.map(citaDto, Cita.class)).thenReturn(citaEsperada);
        when(clienteService.getClienteById(citaEsperada.getCliente().getId()))
                .thenReturn(Optional.of(clienteExistente));
        when(citaRepository.save(any(Cita.class))).thenReturn(citaEsperada);

        Cita resultado = citaService.crearCita(citaDto);

        assertNotNull(resultado);
        assertNotNull(resultado.getCliente());
        assertEquals(clienteExistente.getId(), resultado.getCliente().getId());

        verify(clienteService).getClienteById(citaDto.getCliente().getId());
        verify(clienteService, never()).crearClienteDesdeDto(any());
        verify(citaRepository).save(any(Cita.class));
    }

    /**
     * @deprecated La restriccion de que tiene que haber un cliente se ha quitado. Pero se deja la prueba para futuros casos
     */
    @Deprecated
    @DisplayName("Error al crear cita sin cliente")
    void crearCita_SinCliente_DeberiaLanzarExcepcion() {
        Corte corte = CorteDtoData.VALIDO_CLASICO.getCorte();
        CitaCreateUpdateDto citaDto = CitaUpdateCreateDtoData.SIN_CLIENTE.getCitaDto();
        Cita cita = CitaUpdateCreateDtoData.SIN_CLIENTE.getCita();

        String mensajeMock = "El cliente es obligatorio para crear la cita";

        when(modelMapperMock.map(citaDto, Cita.class)).thenReturn(cita);
        when(corteService.getCorteByIds(anyList())).thenReturn(Collections.singletonList(corte));
        when(messageSource.getMessage("cita.cliente.requerido", null,
                Locale.getDefault())).thenReturn(mensajeMock);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> citaService.crearCita(citaDto));

        assertEquals(mensajeMock, exception.getMessage());

        verify(citaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Crear cita sin cliente sin errores esperados")
    void crearCita_SinCliente_SinErroresEsperados() {
        CitaCreateUpdateDto citaDto = CitaUpdateCreateDtoData.SIN_CLIENTE.getCitaDto();
        Cita cita =  CitaUpdateCreateDtoData.SIN_CLIENTE.getCita();

        when(modelMapperMock.map(citaDto, Cita.class)).thenReturn(cita);
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        Cita resultado = citaService.crearCita(citaDto);

        assertNotNull(resultado);
        assertNull(resultado.getCliente());

        verify(citaRepository).save(any());
        verify(clienteService, never()).getClienteById(any());
        verify(clienteService, never()).crearClienteDesdeDto(any(ClienteDto.class));
    }

    @Test
    @DisplayName("Modificar cita creando un nuevo cliente")
    void modificarCita_ConClienteNoExistente_DeberiaModificarYCrearCliente() {
        Cita citaAnterior = CitaUpdateCreateDtoData
                .VALIDA_CON_PELUQUERO_SIN_CLIENTE_CON_ID.getCita();
        UUID idCitaAModificar = citaAnterior.getId();

        CitaCreateUpdateDto citaDto = CitaUpdateCreateDtoData
                .VALIDA_PARA_MODIFICAR_1.getCitaDto();
        citaDto.setId(idCitaAModificar);

        Cita citaModificada = CitaUpdateCreateDtoData
                .VALIDA_PARA_MODIFICAR_1.getCita();
        citaModificada.setId(idCitaAModificar);

        List<Corte> cortesEsperadas = List.of(
                CorteDtoData.VALIDO_ECONOMICO.getCorte(),
                CorteDtoData.VALIDO_BARBA.getCorte()
        );

        Cliente clienteNuevo = new Cliente();
        clienteNuevo.setNombre(citaDto.getCliente().getNombre());
        clienteNuevo.setTfno(citaDto.getCliente().getTfno());

        when(citaRepository.findById(idCitaAModificar))
                .thenReturn(Optional.of(citaAnterior));

        when(clienteService.getClienteById(citaDto.getCliente().getId()))
                .thenReturn(Optional.empty());

        when(clienteService.crearClienteDesdeDto(any(ClienteDto.class)))
                .thenReturn(clienteNuevo);

        when(corteService.getCorteByIds(citaDto.getIdsCorte().stream().toList()))
                .thenReturn(cortesEsperadas);

        when(citaRepository.save(any(Cita.class)))
                .thenReturn(citaModificada);

        Cita resultado = citaService.modificarCita(idCitaAModificar, citaDto);

        assertNotNull(resultado);

        verify(citaRepository).findById(idCitaAModificar);
        verify(clienteService).getClienteById(citaDto.getCliente().getId());
        verify(clienteService).crearClienteDesdeDto(any(ClienteDto.class));
        verify(corteService).getCorteByIds(citaDto.getIdsCorte().stream().toList());

        verify(citaRepository).save(argThat(citaGuardada ->
                citaGuardada.getId().equals(idCitaAModificar) &&
                        citaGuardada.getFecha().equals(citaDto.getFecha()) &&
                        citaGuardada.getHora().equals(citaDto.getHora()) &&
                        citaGuardada.getCliente() != null &&
                        citaGuardada.getCliente().getNombre().equals(clienteNuevo.getNombre()) &&
                        citaGuardada.getCliente().getTfno().equals(clienteNuevo.getTfno()) &&
                        citaGuardada.getCortes().size() == citaDto.getIdsCorte().size() &&
                        citaGuardada.getPeluqueroAsignado() == null
        ));
    }

    @Test
    @DisplayName("Modificar cita sin crear cliente (cliente ya existe)")
    void modificarCita_ConClienteExistente_NoDeberiaCrearNuevoCliente() {
        CitaCreateUpdateDto citaDto = CitaUpdateCreateDtoData.VALIDA_PARA_MODIFICAR_1.getCitaDto();
        Cita citaEsperada = CitaUpdateCreateDtoData.VALIDA_PARA_MODIFICAR_1.getCita();
        Cliente clienteExistente = ClienteDtoData.VALIDO_JUAN_CON_ID.getCliente();

        when(citaRepository.findById(citaDto.getId())).thenReturn(Optional.of(citaEsperada));
        when(clienteService.getClienteById(citaDto.getCliente().getId()))
                .thenReturn(Optional.of(clienteExistente));
        when(citaRepository.save(any(Cita.class))).thenReturn(citaEsperada);

        Cita resultado = citaService.modificarCita(citaDto.getId(), citaDto);

        assertNotNull(resultado);
        verify(clienteService).getClienteById(citaDto.getCliente().getId());
        verify(clienteService, never()).crearClienteDesdeDto(any());
        verify(citaRepository).save(any(Cita.class));
    }

    @Test
    @DisplayName("Se atender una cita con exito")
    void atenderCita_DeberiaAtenderCita() {
        AtenderCitaRequestDto atenderCitaRequestDto = new AtenderCitaRequestDto();
        atenderCitaRequestDto.setPrecio(new BigDecimal("10.00"));

        Cita cita = CitaUpdateCreateDtoData.VALIDA.getCita();

        when(citaRepository.findById(cita.getId())).thenReturn(Optional.of(cita));
        when(citaRepository.save(cita)).thenReturn(cita);

        Cita resultadoSaveCita = citaService.atenderCita(cita.getId(), atenderCitaRequestDto);

        assertNotNull(resultadoSaveCita);
        assertTrue(resultadoSaveCita.isAtendida());
        assertNotNull(resultadoSaveCita.getPrecio());
        assertEquals(atenderCitaRequestDto.getPrecio(), resultadoSaveCita.getPrecio());

        verify(citaRepository).findById(cita.getId());
        verify(citaRepository).save(cita);
    }
}
