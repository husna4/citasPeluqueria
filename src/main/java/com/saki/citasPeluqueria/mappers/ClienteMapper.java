package com.saki.citasPeluqueria.mappers;

import com.saki.citasPeluqueria.dto.ClienteDto;
import com.saki.citasPeluqueria.dto.ClienteRequestDto;
import com.saki.citasPeluqueria.modelo.Cliente;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class ClienteMapper {
    private final ModelMapper modelMapper;

    public ClienteMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ClienteDto toDto(Cliente cliente) {
        return modelMapper.map(cliente, ClienteDto.class);
    }

    public Cliente toEntity(ClienteRequestDto clienteDto) {
        return modelMapper.map(clienteDto, Cliente.class);
    }
}
