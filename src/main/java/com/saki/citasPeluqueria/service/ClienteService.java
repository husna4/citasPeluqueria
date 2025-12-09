package com.saki.citasPeluqueria.service;

import com.saki.citasPeluqueria.exceptions.ObjectNotFoundException;
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

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente getClienteById(Long id){
        return clienteRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(Cliente.class.getSimpleName(), id));
    }
}
