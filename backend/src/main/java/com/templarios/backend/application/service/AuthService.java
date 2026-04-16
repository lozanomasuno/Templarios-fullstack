package com.templarios.backend.application.service;

import com.templarios.backend.application.dto.AuthResponse;
import com.templarios.backend.application.dto.LoginRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request);
}
