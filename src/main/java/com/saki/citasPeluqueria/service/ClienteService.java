package com.saki.citasPeluqueria.service;

import com.saki.citasPeluqueria.dto.ClienteDto;
import com.saki.citasPeluqueria.modelo.Cliente;
import com.saki.citasPeluqueria.repositorio.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * @author husnain
 */

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public Optional<Cliente> getClienteById(UUID id){
        if(id == null) {
            return Optional.empty();
        }
        return clienteRepository.findById(id);
    }

    public Cliente crearClienteDesdeDto(ClienteDto clienteDto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(clienteDto.getNombre());
        cliente.setTfno(clienteDto.getTfno());
        return cliente;
    }
}
