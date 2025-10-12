package com.saki.citasPeluqueria.dataBuilder;

import com.saki.citasPeluqueria.dto.CitaCreateUpdateDto;
import com.saki.citasPeluqueria.dto.ClienteDto;
import com.saki.citasPeluqueria.modelo.Cita;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;


/**
 * @author husnain
 */
public enum CitaUpdateCreateDtoData {
    SIN_FECHA(new CitaUpdateCreateDtoBuilder()
            .setHora(LocalTime.of(10, 0))
            .anyadirIdCorteACita(UUID.randomUUID())
            .setCliente(ClienteDtoData.VALIDO_SIN_ID.getClienteDto())
            .build()),

    SIN_HORA(new CitaUpdateCreateDtoBuilder()
            .setFecha(LocalDate.now().plusDays(1))
            .anyadirIdCorteACita(UUID.randomUUID())
            .setCliente(ClienteDtoData.VALIDO_SIN_ID.getClienteDto())
            .build()),

    SIN_CORTES(new CitaUpdateCreateDtoBuilder()
            .setFecha(LocalDate.now().plusDays(1))
            .setHora(LocalTime.of(10, 0))
            .setCliente(ClienteDtoData.VALIDO_SIN_ID.getClienteDto())
            .build()),

    SIN_CLIENTE(new CitaUpdateCreateDtoBuilder()
            .setFecha(LocalDate.now().plusDays(1))
            .setHora(LocalTime.of(10, 0))
            .anyadirIdCorteACita(UUID.randomUUID())
            .build()),

    SIN_NOMBRE_CLIENTE(new CitaUpdateCreateDtoBuilder()
            .setFecha(LocalDate.now().plusDays(1))
            .setHora(LocalTime.of(10, 0))
            .setCliente(ClienteDtoData.NOMBRE_VACIO_ID_VACIO.getClienteDto())
            .anyadirIdCorteACita(UUID.randomUUID())
            .build()),

    SIN_FECHA_HORA_CLIENTE_CORTES(new CitaUpdateCreateDtoBuilder()
            .build()),

    VALIDA(new CitaUpdateCreateDtoBuilder()
            .setFecha(LocalDate.now().plusDays(1))
            .setHora(LocalTime.of(10, 0))
            .anyadirIdCorteACita(CorteDtoData.VALIDO_CLASICO.getCorteDto().getId())
            .setCliente(ClienteDtoData.VALIDO_SIN_ID.getClienteDto())
            .build()),

    VALIDA_PARA_MODIFICAR_1(new CitaUpdateCreateDtoBuilder()
            .setId(UUID.randomUUID())
            .setFecha(LocalDate.now().plusDays(1))
            .setHora(LocalTime.of(10, 0))
            .anyadirIdCorteACita(CorteDtoData.VALIDO_ECONOMICO.getCorteDto().getId())
            .anyadirIdCorteACita(CorteDtoData.VALIDO_BARBA.getCorteDto().getId())
            .setCliente(ClienteDtoData.VALIDO_SIN_ID.getClienteDto())
            .build()),

    VALIDA_PARA_MODIFICAR_2_CON_ID_CLIENTE(new CitaUpdateCreateDtoBuilder()
            .setId(UUID.randomUUID())
            .setFecha(LocalDate.now().plusDays(1))
            .setHora(LocalTime.of(10, 0))
            .anyadirIdCorteACita(CorteDtoData.VALIDO_ECONOMICO.getCorteDto().getId())
            .anyadirIdCorteACita(CorteDtoData.VALIDO_BARBA.getCorteDto().getId())
            .setCliente(ClienteDtoData.VALIDO_JUAN_CON_ID.getClienteDto())
            .build()),


    VALIDA_CON_PELUQUERO_SIN_ID_CLIENTE(new CitaUpdateCreateDtoBuilder()
            .setFecha(LocalDate.now().plusDays(1))
            .setHora(LocalTime.of(10, 0))
            .anyadirIdCorteACita(CorteDtoData.VALIDO_CLASICO.getCorteDto().getId())
            .setCliente(ClienteDtoData.VALIDO_SIN_ID.getClienteDto())
            .setIdPeluqueroAsignado(PeluqueroDtoData.VALIDO_ANA.getPeluqueroDto().getId())
            .build()),

    VALIDA_CON_PELUQUERO_SIN_CLIENTE_CON_ID(new CitaUpdateCreateDtoBuilder()
            .setId(UUID.randomUUID())
            .setFecha(LocalDate.now().plusDays(1))
            .setHora(LocalTime.of(10, 0))
            .anyadirIdCorteACita(CorteDtoData.VALIDO_CLASICO.getCorteDto().getId())
            .setCliente(ClienteDtoData.VALIDO_SIN_ID.getClienteDto())
            .setIdPeluqueroAsignado(PeluqueroDtoData.VALIDO_ANA.getPeluqueroDto().getId())
            .build()),

    VALIDA_CON_PELUQUERO_CON_ID_CLIENTE(new CitaUpdateCreateDtoBuilder()
            .setFecha(LocalDate.now().plusDays(1))
            .setHora(LocalTime.of(10, 0))
            .anyadirIdCorteACita(CorteDtoData.VALIDO_CLASICO.getCorteDto().getId())
            .setCliente(ClienteDtoData.VALIDO_JUAN_CON_ID.getClienteDto())
            .setIdPeluqueroAsignado(PeluqueroDtoData.VALIDO_ANA.getPeluqueroDto().getId())
            .build());

    private CitaCreateUpdateDto citaDto;

    private ModelMapper modelMapper;

    CitaUpdateCreateDtoData(CitaCreateUpdateDto citaDto) {
        this.citaDto = citaDto;
        modelMapper = new ModelMapper();
    }

    public CitaCreateUpdateDto getCitaDto() {
        return citaDto;
    }

    public Cita getCita(){
        return modelMapper.map(citaDto, Cita.class);
    }

    public static class CitaUpdateCreateDtoBuilder {
        private CitaCreateUpdateDto citaDto;

        public CitaUpdateCreateDtoBuilder(){
            citaDto = new CitaCreateUpdateDto();
        }

        public CitaUpdateCreateDtoBuilder setId(UUID id) {
            citaDto.setId(id);
            return this;
        }

        public CitaUpdateCreateDtoBuilder setFecha(LocalDate fecha){
            citaDto.setFecha(fecha);
            return this;
        }

        public CitaUpdateCreateDtoBuilder setHora(LocalTime hora) {
            citaDto.setHora(hora);
            return this;
        }

        public CitaUpdateCreateDtoBuilder setCliente(ClienteDto clienteDto) {
            citaDto.setCliente(clienteDto);

            return this;
        }

        public CitaUpdateCreateDtoBuilder setIdsCortes(Set<UUID> idsCortes) {
            citaDto.setIdsCorte(idsCortes);

            return this;
        }

        public CitaUpdateCreateDtoBuilder anyadirIdCorteACita(UUID idCorte) {
            citaDto.addIdCorte(idCorte);
            return this;
        }

        public CitaUpdateCreateDtoBuilder setAtendida(boolean atendida) {
            citaDto.setAtendida(atendida);
            return this;
        }

        public CitaUpdateCreateDtoBuilder setIdPeluqueroAsignado(UUID idPeluqueroAsignado) {
            citaDto.setIdPeluqueroAsignado(idPeluqueroAsignado);
            return this;
        }

        public CitaCreateUpdateDto build() {
            return citaDto;
        }
    }
}
