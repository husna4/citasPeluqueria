package com.saki.citasPeluqueria.dataBuilder;


import com.saki.citasPeluqueria.dto.ClienteDto;
import com.saki.citasPeluqueria.modelo.Cliente;
import org.modelmapper.ModelMapper;

import java.util.UUID;

/**
 * @author husnain
 */
public enum ClienteDtoData {
    SIN_NOMBRE(new ClienteDtoBuilder()
            .setTfno("123456789")
            .build()),

    NOMBRE_VACIO_ID_VACIO(new ClienteDtoBuilder()
            .setNombre("   ")
            .setTfno("123456789")
            .build()),

    NOMBRE_LARGO_SIN_ID(new ClienteDtoBuilder()
            .setNombre("a".repeat(51)) // 51 caracteres (máximo es 50)
            .setTfno("123456789")
            .build()),

    TFNO_LARGO_SIN_ID(new ClienteDtoBuilder()
            .setNombre("Juan Pérez")
            .setTfno("a".repeat(17)) // 17 caracteres (máximo es 16)
            .build()),

    VALIDO_SIN_ID(new ClienteDtoBuilder()
            .setNombre("Juan Pérez")
            .setTfno("123456789")
            .build()),

    NOMBRE_VACIO_CON_ID(new ClienteDtoBuilder()
            .setId(UUID.fromString("20202020-2020-2020-2020-202020202020"))
            .setNombre("   ")
            .setTfno("123456789")
            .build()),

    NOMBRE_LARGO_CON_ID(new ClienteDtoBuilder()
            .setId(UUID.fromString("30303030-3030-3030-3030-303030303030"))
            .setNombre("a".repeat(51)) // 51 caracteres (máximo es 50)
            .setTfno("123456789")
            .build()),

    TFNO_LARGO_CON_ID(new ClienteDtoBuilder()
            .setId(UUID.fromString("40404040-4040-4040-4040-404040404040"))
            .setNombre("Juan Pérez")
            .setTfno("a".repeat(17)) // 17 caracteres (máximo es 16)
            .build()),

    VALIDO_JUAN_CON_ID(new ClienteDtoBuilder()
            .setId(UUID.fromString("11111111-2222-3333-4444-555555555555"))
            .setNombre("Juan Pérez")
            .setTfno("123456789")
            .build()),

    VALIDO_MARIA_CON_ID(new ClienteDtoBuilder()
            .setId(UUID.fromString("22222222-3333-4444-5555-666666666666"))
            .setNombre("María González")
            .setTfno("987654321")
            .build()),

    VALIDO_CARLOS_SIN_ID(new ClienteDtoBuilder()
            .setNombre("Carlos Jiménez")
            .setTfno("456789123")
            .build()),

    VALIDO_ANA_CON_ID(new ClienteDtoBuilder()
            .setId(UUID.fromString("44444444-5555-6666-7777-888888888888"))
            .setNombre("Ana Martín")
            .setTfno("321654987")
            .build()),

    VALIDO_LUIS_CON_ID(new ClienteDtoBuilder()
            .setId(UUID.fromString("55555555-6666-7777-8888-999999999999"))
            .setNombre("Luis Fernando Ruiz")
            .setTfno("789123456")
            .build());

    private final ClienteDto clienteDto;

    private final ModelMapper modelMapper;

    ClienteDtoData(ClienteDto clienteDto) {
        this.clienteDto = clienteDto;
        modelMapper = new ModelMapper();
    }

    public ClienteDto getClienteDto() {
        return clienteDto;
    }

    public Cliente getCliente() {
        return modelMapper.map(clienteDto, Cliente.class);
    }

    public static class ClienteDtoBuilder {
        private ClienteDto clienteDto;

        public ClienteDtoBuilder() {
            clienteDto = new ClienteDto();
        }

        public ClienteDtoBuilder setId(UUID id) {
            clienteDto.setId(id);
            return this;
        }

        public ClienteDtoBuilder setNombre(String nombre) {
            clienteDto.setNombre(nombre);
            return this;
        }

        public ClienteDtoBuilder setTfno(String tfno) {
            clienteDto.setTfno(tfno);
            return this;
        }

        public ClienteDto build() {
            return clienteDto;
        }
    }
}
