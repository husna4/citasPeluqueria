package com.saki.citasPeluqueria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for user registration request
 * 
 * @author husnain
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    @NotBlank(message = "{auth.username.requerido}")
    private String username;

    @NotBlank(message = "{auth.password.requerido}")
    private String password;

    @NotBlank(message = "{auth.role.requerido}")
    @Pattern(regexp = "ADMIN|USER", message = "{auth.role.invalido}")
    private String role;
}
