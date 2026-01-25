package com.saki.citasPeluqueria.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a user in the system
 * 
 * @author husnain
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{usuario.username.requerido}")
    @Size(max = 50, message = "{usuario.username.tamanyo}")
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @NotBlank(message = "{usuario.password.requerido}")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "{usuario.role.requerido}")
    @Column(nullable = false, length = 20)
    private String role; // "ADMIN" or "USER"

    @Column(nullable = false)
    private boolean enabled = true;
}
