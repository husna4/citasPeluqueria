package com.saki.citasPeluqueria.service;

import com.saki.citasPeluqueria.dto.ClienteDto;
import com.saki.citasPeluqueria.dto.ClienteRequestDto;
import com.saki.citasPeluqueria.exceptions.ObjectNotFoundException;
import com.saki.citasPeluqueria.mappers.ClienteMapper;
import com.saki.citasPeluqueria.modelo.Cliente;
import com.saki.citasPeluqueria.repositorio.ClienteRepository;
import com.saki.citasPeluqueria.util.StringUtil;
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
    private final ClienteMapper clienteMapper;

    public ClienteService(ClienteRepository clienteRepository,
                          ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    public Cliente getClienteById(Long id){
        return clienteRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(Cliente.class.getSimpleName(), id));
    }

    public Cliente crearCliente(ClienteRequestDto clienteRequestDto){
        return clienteRepository.save(clienteMapper.toEntity(clienteRequestDto));
    }

    public Optional<Cliente> getByTfno(String tfno) {
        if(StringUtil.isNullOrEmpty(tfno.trim())) {
            return Optional.empty();
        }

        return clienteRepository.findByTfno(tfno);
    }

    public Optional<Cliente> getByTfnoAndNombre(String tfno, String nombre) {
        if(StringUtil.isNullOrEmpty(tfno) || StringUtil.isNullOrEmpty(nombre)) {
            return Optional.empty();
        }

        return clienteRepository.findByTfnoAndNombre(tfno.trim(), nombre.trim());
    }
}
