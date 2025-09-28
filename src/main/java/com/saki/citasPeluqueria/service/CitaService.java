package com.saki.citasPeluqueria.service;

import com.saki.citasPeluqueria.dto.CitaCreateUpdateDto;
import com.saki.citasPeluqueria.dto.ClienteDto;
import com.saki.citasPeluqueria.exceptions.ObjectNotFoundException;
import com.saki.citasPeluqueria.modelo.Cita;
import com.saki.citasPeluqueria.modelo.Cliente;
import com.saki.citasPeluqueria.modelo.Corte;
import com.saki.citasPeluqueria.repositorio.CitaRepository;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author husnain
 */

@Service
public class CitaService {
    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CorteService corteService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PeluqueroService peluqueroService;

    @Autowired
    private MessageSource messageSource;

    public List<Cita> getCitas(){
        return citaRepository.findAll();
    }

    public List<Cita> getCitasSinAtender(){
        return citaRepository.findByAtendidaOrderByFechaAscHoraAsc(false);
    }

    public Optional<Cita> getCitaById(UUID id) {
        if(id == null) {
            return Optional.empty();
        }

        return citaRepository.findById(id);
    }

    public Cita crearCita(@NotNull CitaCreateUpdateDto citaDto) throws IllegalArgumentException {
        Cita cita = modelMapper.map(citaDto, Cita.class);
        anyadirCortesACita(cita, citaDto);
        anaydirClienteACitaDesdeDto(cita, citaDto);

        if(citaDto.getIdPeluqueroAsignado() != null) {
            anaydirPeluqueroAsignadoACita(cita, citaDto);
        }

        return citaRepository.save(cita);
    }

    public Cita modificarCita(@NotNull UUID id, @NotNull CitaCreateUpdateDto citaDto) throws IllegalArgumentException {
        Cita cita = getCitaById(id).orElseThrow(() ->
                new ObjectNotFoundException(messageSource, Cita.class.getSimpleName(), id));

        cita.setFecha(citaDto.getFecha());
        cita.setHora(citaDto.getHora());
        cita.setAtendida(citaDto.isAtendida());

        anyadirCortesACita(cita, citaDto);
        anaydirClienteACitaDesdeDto(cita, citaDto);

        if(citaDto.getIdPeluqueroAsignado() != null) {
            anaydirPeluqueroAsignadoACita(cita, citaDto);
        }
        else{
           cita.setPeluqueriAsignado(null);
        }

        return citaRepository.save(cita);
    }

    public void eliminarCita(@NotNull UUID id){
        citaRepository.deleteById(id);
    }

    private void anyadirCortesACita(Cita cita, CitaCreateUpdateDto citaDto) {
        List<Corte> cortes = corteService.getCorteByIds(citaDto.getIdsCorte().stream().toList());
        cita.setCortes(new HashSet<>(cortes));
    }

    private void anaydirClienteACitaDesdeDto(Cita cita, CitaCreateUpdateDto citaDto) throws IllegalArgumentException {
        if(citaDto.getCliente() == null)  {
            throw new IllegalArgumentException(messageSource.getMessage("cita.cliente.requerido",
                    null, Locale.getDefault()));
        }

        Cliente cliente = clienteService.getClienteById(citaDto.getCliente().getId())
                .orElseGet(() -> crearYObtenerNuevaInstanciaCliente(citaDto.getCliente()));

        cita.setCliente(cliente);
    }

    private Cliente crearYObtenerNuevaInstanciaCliente(ClienteDto clienteDto) {
        Cliente cliente = new Cliente();

        cliente.setNombre(clienteDto.getNombre());
        cliente.setTfno(clienteDto.getTfno());

        return cliente;
    }

    private void anaydirPeluqueroAsignadoACita(Cita cita, CitaCreateUpdateDto citaDto) {
        peluqueroService.getPeluqueroById(citaDto.getIdPeluqueroAsignado())
                .ifPresent(cita::setPeluqueriAsignado);

    }

    public Cita atenderCita(@NotNull UUID idCita) throws ObjectNotFoundException {
        Cita cita = citaRepository.findById(idCita).orElseThrow(() ->
                new ObjectNotFoundException(messageSource, Cita.class.getSimpleName(), idCita));

        cita.setAtendida(true);

        return citaRepository.save(cita);
    }
}
