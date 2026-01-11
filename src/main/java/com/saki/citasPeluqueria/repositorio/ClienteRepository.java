package com.saki.citasPeluqueria.repositorio;

import com.saki.citasPeluqueria.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByTfno(String tfno);

    @Query("SELECT c From Cliente c WHERE c.tfno = :tfno AND UPPER(c.nombre) = UPPER(:nombre)")
    Optional<Cliente> findByTfnoAndNombre(@Param("tfno") String tfno, @Param("nombre") String nombre);
}
