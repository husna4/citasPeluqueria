package com.saki.citasPeluqueria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author husnain
 */
public class CorteDto {
        private UUID id;

        @NotBlank(message = "{corte.error.nombre.requerido}")
        @Size(max = 50)
        private String nombre;

        @NotNull(message = "{corte.error.precio.requerido}")
        private BigDecimal precio;

        public UUID getId() {
                return id;
        }

        public void setId(UUID id) {
                this.id = id;
        }

        public String getNombre() {
                return nombre;
        }

        public void setNombre(String nombre) {
                this.nombre = nombre;
        }

        public BigDecimal getPrecio() {
                return precio;
        }

        public void setPrecio(BigDecimal precio) {
                this.precio = precio;
        }
}
