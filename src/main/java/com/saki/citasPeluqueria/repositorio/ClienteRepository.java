package com.saki.citasPeluqueria.repositorio;

import com.saki.citasPeluqueria.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
}
