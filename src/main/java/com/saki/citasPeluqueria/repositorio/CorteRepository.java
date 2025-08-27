package com.saki.citasPeluqueria.repositorio;

import com.saki.citasPeluqueria.modelo.Corte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author husnain
 */
public interface CorteRepository extends JpaRepository<Corte, UUID> {

}
