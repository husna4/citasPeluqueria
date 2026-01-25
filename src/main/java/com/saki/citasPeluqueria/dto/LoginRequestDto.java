package com.saki.citasPeluqueria.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for login request
 * 
 * @author husnain
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "{auth.username.requerido}")
    private String username;

    @NotBlank(message = "{auth.password.requerido}")
    private String password;
}
