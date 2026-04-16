package com.templarios.backend.application.service.impl;

import com.templarios.backend.application.dto.AuthResponse;
import com.templarios.backend.application.dto.LoginRequest;
import com.templarios.backend.application.service.AuthService;
import com.templarios.backend.infrastructure.security.JwtService;
import com.templarios.backend.shared.exception.UnauthorizedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails user = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(user);
            String role = user.getAuthorities().stream().findFirst().map(Object::toString).orElse("ROLE_USER");

            return new AuthResponse(token, "Bearer", user.getUsername(), role);
        } catch (BadCredentialsException ex) {
            throw new UnauthorizedException("Credenciales invalidas");
        }
    }
}
