package com.saki.citasPeluqueria.modelo;

import com.saki.citasPeluqueria.dataBuilder.CitaUpdateCreateDtoData;
import com.saki.citasPeluqueria.dataBuilder.ClienteDtoData;
import com.saki.citasPeluqueria.dataBuilder.CorteDtoData;
import com.saki.citasPeluqueria.dataBuilder.PeluqueroDtoData;
import com.saki.citasPeluqueria.dto.CitaCreateUpdateDto;
import com.saki.citasPeluqueria.dto.ClienteDto;
import com.saki.citasPeluqueria.dto.CorteDto;
import com.saki.citasPeluqueria.exceptions.ObjectNotFoundException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

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

    @Autowired
    private ModelMapper modelMapper;

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
        CorteDto corteDto = CorteDtoData.VALIDO_CLASICO.getCorteDto();

        Corte corte = modelMapper.map(corteDto, Corte.class);
        Peluquero peluquero = modelMapper.map(PeluqueroDtoData.VALIDO_ANA.getPeluqueroDto(), Peluquero.class);
        CitaCreateUpdateDto citaDto = CitaUpdateCreateDtoData.VALIDA_CON_PELUQUERO_SIN_ID_CLIENTE.getCitaDto();
        Cita cita = modelMapper.map(citaDto, Cita.class);

        when(modelMapperMock.map(citaDto, Cita.class)).thenReturn(cita);
        when(corteService.getCorteByIds(anyList())).thenReturn(Collections.singletonList(corte));
        when(clienteService.getClienteById(cita.getCliente().getId())).thenReturn(Optional.empty()); // debería crear cliente nuevo
        when(peluqueroService.getPeluqueroById(citaDto.getIdPeluqueroAsignado())).thenReturn(Optional.of(peluquero));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        Cita resultado = citaService.crearCita(citaDto);

        // Then
        assertNotNull(resultado);
        assertEquals(cita.getId(), resultado.getId());
        assertNotNull(resultado.getCliente());
        assertEquals(cita.getCliente().getNombre(), resultado.getCliente().getNombre());
        assertNotNull(resultado.getPeluqueriAsignado());
        assertEquals(cita.getPeluqueriAsignado().getNombre(), resultado.getPeluqueriAsignado().getNombre());


        verify(corteService).getCorteByIds(anyList());
        verify(clienteService).getClienteById(citaDto.getCliente().getId());
        verify(peluqueroService).getPeluqueroById(citaDto.getIdPeluqueroAsignado());
        verify(citaRepository).save(any(Cita.class));
    }

    @Test
    @DisplayName("Crear nueva cita obteniendo cliente de la BD")
    void crearCita_ClienteNoExistente_NoDeberiaCrearNuevoCliente() {
        CorteDto corteDto = CorteDtoData.VALIDO_CLASICO.getCorteDto();

        Corte corte = modelMapper.map(corteDto, Corte.class);
        Peluquero peluquero = modelMapper.map(PeluqueroDtoData.VALIDO_ANA.getPeluqueroDto(), Peluquero.class);
        CitaCreateUpdateDto citaDto = CitaUpdateCreateDtoData.VALIDA_CON_PELUQUERO_CON_ID_CLIENTE.getCitaDto();
        Cita cita = modelMapper.map(citaDto, Cita.class);
        ClienteDto clienteDto = ClienteDtoData.VALIDO_JUAN_CON_ID.getClienteDto();
        Cliente cliente = modelMapper.map(clienteDto, Cliente.class);

        cita.getCliente().setNombre(null); // Quitamos el nombre del cliente para ver luego que se obtiene de la BD

        when(modelMapperMock.map(citaDto, Cita.class)).thenReturn(cita);
        when(corteService.getCorteByIds(anyList())).thenReturn(Collections.singletonList(corte));
        when(clienteService.getClienteById(cita.getCliente().getId())).thenReturn(Optional.ofNullable(cliente));
        when(peluqueroService.getPeluqueroById(citaDto.getIdPeluqueroAsignado())).thenReturn(Optional.of(peluquero));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        Cita resultado = citaService.crearCita(citaDto);

        assertNotNull(resultado);
        assertEquals(cita.getId(), resultado.getId());
        assertNotNull(resultado.getCliente());
        assertEquals(clienteDto.getNombre(), resultado.getCliente().getNombre());
        assertNotNull(resultado.getPeluqueriAsignado());
        assertEquals(cita.getPeluqueriAsignado().getNombre(), resultado.getPeluqueriAsignado().getNombre());

        verify(corteService).getCorteByIds(anyList());
        verify(clienteService).getClienteById(clienteDto.getId());
        verify(peluqueroService).getPeluqueroById(citaDto.getIdPeluqueroAsignado());
        verify(citaRepository).save(any(Cita.class));
    }

    @Test
    @DisplayName("Error al crear cita sin cliente")
    void crearCita_SinCliente_DeberiaLanzarExcepcion() {
        CorteDto corteDto = CorteDtoData.VALIDO_CLASICO.getCorteDto();

        Corte corte = modelMapper.map(corteDto, Corte.class);
        CitaCreateUpdateDto citaDto = CitaUpdateCreateDtoData.SIN_CLIENTE.getCitaDto();
        Cita cita = modelMapper.map(citaDto, Cita.class);
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
    @DisplayName("Modificar la cita creando un nuevo cliente con éxito")
    void modificarCita_ConDatosValidos_DeberiaModificarCita_creandoNuevoCliente() {
        CitaCreateUpdateDto citaDto = CitaUpdateCreateDtoData.VALIDA_PARA_MODIFICAR_1.getCitaDto();
        Cita cita = modelMapper.map(citaDto, Cita.class);
        List<Corte> cortes = new ArrayList<>();
        UUID idCitaAModifcar = citaDto.getId();

        cortes.add(modelMapper.map(CorteDtoData.VALIDO_ECONOMICO.getCorteDto(), Corte.class));
        cortes.add(modelMapper.map(CorteDtoData.VALIDO_BARBA.getCorteDto(), Corte.class));

        when(citaRepository.findById(idCitaAModifcar)).thenReturn(Optional.of(cita));
        when(corteService.getCorteByIds(anyList())).thenReturn(cortes);
        when(citaRepository.save(cita)).thenReturn(cita);
        when(clienteService.getClienteById(cita.getCliente().getId())).thenReturn(Optional.empty());

        Cita resultado = citaService.modificarCita(idCitaAModifcar, citaDto);

        assertNotNull(resultado);
        assertEquals(citaDto.getFecha(), resultado.getFecha());
        assertEquals(citaDto.getHora(), resultado.getHora());
        assertNotNull(citaDto.getCliente());
        assertEquals(citaDto.getCliente().getNombre(), resultado.getCliente().getNombre());
        assertNotNull(resultado.getCortes());
        assertEquals(2, resultado.getCortes().size());

        verify(citaRepository).findById(citaDto.getId());
        verify(corteService).getCorteByIds(anyList());
        verify(citaRepository).save(cita);
        verify(clienteService).getClienteById(cita.getCliente().getId());
    }

    @Test
    @DisplayName("Error al modificar cliente sin id")
    void modificarCita_SinId_DeberiaLanzarExcepcion() {
        CitaCreateUpdateDto citaDto = CitaUpdateCreateDtoData.VALIDA_PARA_MODIFICAR_1.getCitaDto();
        String mensajeErrorEsperado = "No se ha podido encontrar cita con id " + citaDto.getId();
        UUID idCitaAModifcar = citaDto.getId();

        when(citaRepository.findById(idCitaAModifcar)).thenReturn(Optional.empty());
        when(messageSource.getMessage("objeto.no.encontrado",
                new Object[]{Cita.class.getSimpleName(), citaDto.getId()},
                Locale.getDefault())).thenReturn(mensajeErrorEsperado);

        ObjectNotFoundException exception =
                assertThrows(ObjectNotFoundException.class, () -> citaService.modificarCita(idCitaAModifcar, citaDto));

        assertEquals(mensajeErrorEsperado, exception.getMessage());

        verify(citaRepository).findById(citaDto.getId());
        verify(citaRepository, never()).save(any());
        verify(messageSource).getMessage("objeto.no.encontrado",
                new Object[]{Cita.class.getSimpleName(), citaDto.getId()}, Locale.getDefault());
    }
}
