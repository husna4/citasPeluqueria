package com.saki.citasPeluqueria.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for login response containing JWT token
 * 
 * @author husnain
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private String token;
    private String type = "Bearer";
    private String username;
    private String role;

    public LoginResponseDto(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }
}
