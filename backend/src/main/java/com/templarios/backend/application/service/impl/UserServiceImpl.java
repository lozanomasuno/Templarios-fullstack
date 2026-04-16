package com.templarios.backend.application.service.impl;

import com.templarios.backend.application.dto.RegisterRequest;
import com.templarios.backend.application.dto.UserResponse;
import com.templarios.backend.application.service.UserService;
import com.templarios.backend.domain.model.AppUser;
import com.templarios.backend.domain.model.Role;
import com.templarios.backend.domain.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(u -> new UserResponse(u.getId(), u.getUsername(), u.getRole().name()))
                .toList();
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya existe");
        }
        Role role = Role.valueOf(request.getRole());
        AppUser user = new AppUser(null, request.getUsername(),
                passwordEncoder.encode(request.getPassword()), role);
        userRepository.save(user);
        return new UserResponse(user.getId(), user.getUsername(), user.getRole().name());
    }
}
