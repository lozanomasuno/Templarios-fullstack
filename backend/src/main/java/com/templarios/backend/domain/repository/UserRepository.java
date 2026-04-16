package com.templarios.backend.domain.repository;

import com.templarios.backend.domain.model.AppUser;

import java.util.Optional;

public interface UserRepository {

    Optional<AppUser> findByUsername(String username);

    void save(AppUser user);
}
