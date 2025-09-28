package com.saki.citasPeluqueria.repositorio;

import com.saki.citasPeluqueria.modelo.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CitaRepository extends JpaRepository<Cita, UUID> {

    List<Cita> findByAtendidaOrderByFechaAscHoraAsc(boolean atendida);
}
