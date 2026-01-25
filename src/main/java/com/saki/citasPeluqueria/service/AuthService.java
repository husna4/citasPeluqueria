package com.saki.citasPeluqueria.service;

import com.saki.citasPeluqueria.dto.LoginRequestDto;
import com.saki.citasPeluqueria.dto.LoginResponseDto;
import com.saki.citasPeluqueria.dto.RegisterRequestDto;
import com.saki.citasPeluqueria.modelo.Usuario;
import com.saki.citasPeluqueria.repositorio.UsuarioRepository;
import com.saki.citasPeluqueria.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for authentication operations
 * 
 * @author husnain
 */
@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MessageSource messageSource;

    /**
     * Authenticate user and generate JWT token
     */
    public LoginResponseDto login(LoginRequestDto loginRequest) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        // Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

        // Generate JWT token
        String token = jwtUtil.generateToken(userDetails);

        // Get user role
        Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException(
                        messageSource.getMessage("auth.usuario.no.encontrado", null, LocaleContextHolder.getLocale())));

        return new LoginResponseDto(token, usuario.getUsername(), usuario.getRole());
    }

    /**
     * Register a new user (admin only)
     */
    public Usuario register(RegisterRequestDto registerRequest) {
        // Check if username already exists
        if (usuarioRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException(
                    messageSource.getMessage("auth.username.existe", null, LocaleContextHolder.getLocale()));
        }

        // Create new user
        Usuario usuario = new Usuario();
        usuario.setUsername(registerRequest.getUsername());
        usuario.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        usuario.setRole(registerRequest.getRole());
        usuario.setEnabled(true);

        return usuarioRepository.save(usuario);
    }
}
