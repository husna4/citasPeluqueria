package com.saki.citasPeluqueria.dataBuilder;

import com.saki.citasPeluqueria.dto.CorteDto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author husnain
 */
public enum CorteDtoData {
    SIN_ID(new CorteDtoBuilder()
            .setNombre("Corte clásico")
            .setPrecio(new BigDecimal("15.50"))
            .build()),

    SIN_NOMBRE(new CorteDtoBuilder()
            .setId(UUID.fromString("c0000001-0001-0001-0001-000000000001"))
            .setPrecio(new BigDecimal("15.50"))
            .build()),

    NOMBRE_VACIO(new CorteDtoBuilder()
            .setId(UUID.fromString("c0000002-0002-0002-0002-000000000002"))
            .setNombre("   ")
            .setPrecio(new BigDecimal("15.50"))
            .build()),

    NOMBRE_LARGO(new CorteDtoBuilder()
            .setId(UUID.fromString("c0000003-0003-0003-0003-000000000003"))
            .setNombre("a".repeat(51)) // 51 caracteres (máximo es 50)
            .setPrecio(new BigDecimal("15.50"))
            .build()),

    SIN_PRECIO(new CorteDtoBuilder()
            .setId(UUID.fromString("c0000004-0004-0004-0004-000000000004"))
            .setNombre("Corte clásico")
            .build()),

    PRECIO_NEGATIVO(new CorteDtoBuilder()
            .setId(UUID.fromString("c0000005-0005-0005-0005-000000000005"))
            .setNombre("Corte clásico")
            .setPrecio(new BigDecimal("-5.00"))
            .build()),

    PRECIO_CERO(new CorteDtoBuilder()
            .setId(UUID.fromString("c0000006-0006-0006-0006-000000000006"))
            .setNombre("Corte clásico")
            .setPrecio(BigDecimal.ZERO)
            .build()),

    VALIDO_CLASICO(new CorteDtoBuilder()
            .setId(UUID.fromString("c1111111-1111-1111-1111-111111111111"))
            .setNombre("Corte clásico")
            .setPrecio(new BigDecimal("15.50"))
            .build()),

    VALIDO_MODERNO(new CorteDtoBuilder()
            .setId(UUID.fromString("c2222222-2222-2222-2222-222222222222"))
            .setNombre("Corte moderno")
            .setPrecio(new BigDecimal("25.00"))
            .build()),

    VALIDO_PREMIUM(new CorteDtoBuilder()
            .setId(UUID.fromString("c3333333-3333-3333-3333-333333333333"))
            .setNombre("Corte premium con estilizado")
            .setPrecio(new BigDecimal("45.75"))
            .build()),

    VALIDO_ECONOMICO(new CorteDtoBuilder()
            .setId(UUID.fromString("c4444444-4444-4444-4444-444444444444"))
            .setNombre("Corte básico")
            .setPrecio(new BigDecimal("10.00"))
            .build()),

    VALIDO_BARBA(new CorteDtoBuilder()
            .setId(UUID.fromString("c5555555-5555-5555-5555-555555555555"))
            .setNombre("Arreglo de barba")
            .setPrecio(new BigDecimal("8.00"))
            .build());

    private final CorteDto corteDto;

    CorteDtoData(CorteDto corteDto) {
        this.corteDto = corteDto;
    }

    public CorteDto getCorteDto() {
        return corteDto;
    }

    public static class CorteDtoBuilder {
        private final CorteDto corteDto;

        public CorteDtoBuilder() {
            corteDto = new CorteDto();
        }

        public CorteDtoBuilder setId(UUID id) {
            corteDto.setId(id);
            return this;
        }

        public CorteDtoBuilder setNombre(String nombre) {
            corteDto.setNombre(nombre);
            return this;
        }

        public CorteDtoBuilder setPrecio(BigDecimal precio) {
            corteDto.setPrecio(precio);
            return this;
        }

        public CorteDto build() {
            return corteDto;
        }
    }
}