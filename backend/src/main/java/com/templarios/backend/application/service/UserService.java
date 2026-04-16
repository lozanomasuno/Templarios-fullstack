package com.templarios.backend.application.service;

import com.templarios.backend.application.dto.RegisterRequest;
import com.templarios.backend.application.dto.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> findAll();

    UserResponse register(RegisterRequest request);
}
