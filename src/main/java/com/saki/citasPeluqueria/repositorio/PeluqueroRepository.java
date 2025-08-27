package com.saki.citasPeluqueria.repositorio;


import com.saki.citasPeluqueria.modelo.Peluquero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author husnain
 */
public interface PeluqueroRepository extends JpaRepository<Peluquero, UUID> {
}
