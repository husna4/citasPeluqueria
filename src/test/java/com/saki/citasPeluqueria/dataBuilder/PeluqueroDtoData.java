package com.saki.citasPeluqueria.dataBuilder;

import com.saki.citasPeluqueria.dto.PeluqueroDto;
import com.saki.citasPeluqueria.modelo.Peluquero;
import org.modelmapper.ModelMapper;

import java.util.UUID;

/**
 * @author husnain
 */
public enum PeluqueroDtoData {
    SIN_NOMBRE(new PeluqueroDtoBuilder()
            .setId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
            .setTfno("987654321")
            .build()),

    NOMBRE_VACIO(new PeluqueroDtoBuilder()
            .setId(UUID.fromString("22222222-2222-2222-2222-222222222222"))
            .setNombre("   ")
            .setTfno("987654321")
            .build()),

    NOMBRE_LARGO(new PeluqueroDtoBuilder()
            .setId(UUID.fromString("33333333-3333-3333-3333-333333333333"))
            .setNombre("a".repeat(51)) // 51 caracteres (máximo es 50)
            .setTfno("987654321")
            .build()),

    TFNO_LARGO(new PeluqueroDtoBuilder()
            .setId(UUID.fromString("44444444-4444-4444-4444-444444444444"))
            .setNombre("María García")
            .setTfno("a".repeat(17)) // 17 caracteres (máximo es 16)
            .build()),

    VALIDO_MARIA(new PeluqueroDtoBuilder()
            .setId(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
            .setNombre("María García")
            .setTfno("987654321")
            .build()),

    VALIDO_CARLOS(new PeluqueroDtoBuilder()
            .setId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"))
            .setNombre("Carlos Rodríguez")
            .setTfno("654321987")
            .build()),

    VALIDO_ANA(new PeluqueroDtoBuilder()
            .setId(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc"))
            .setNombre("Ana López")
            .setTfno("123987456")
            .build()),

    VALIDO_SENIOR(new PeluqueroDtoBuilder()
            .setId(UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd"))
            .setNombre("José Antonio Martínez")
            .setTfno("555123456")
            .build()),

    VALIDO_LAURA(new PeluqueroDtoBuilder()
            .setId(UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee"))
            .setNombre("Laura Fernández")
            .setTfno("666777888")
            .build());

    private final PeluqueroDto peluqueroDto;

    private final ModelMapper modelMapper;

    PeluqueroDtoData(PeluqueroDto peluqueroDto) {
        this.peluqueroDto = peluqueroDto;
        modelMapper = new ModelMapper();
    }

    public PeluqueroDto getPeluqueroDto() {
        return peluqueroDto;
    }

    public Peluquero getPeluquero() {
        return modelMapper.map(peluqueroDto, Peluquero.class);
    }

    public static class PeluqueroDtoBuilder {
        private final PeluqueroDto peluqueroDto;

        public PeluqueroDtoBuilder() {
            peluqueroDto = new PeluqueroDto();
        }

        public PeluqueroDtoBuilder setId(UUID id) {
            peluqueroDto.setId(id);
            return this;
        }

        public PeluqueroDtoBuilder setNombre(String nombre) {
            peluqueroDto.setNombre(nombre);
            return this;
        }

        public PeluqueroDtoBuilder setTfno(String tfno) {
            peluqueroDto.setTfno(tfno);
            return this;
        }

        public PeluqueroDto build() {
            return peluqueroDto;
        }
    }
}