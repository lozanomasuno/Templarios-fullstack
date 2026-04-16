package com.templarios.backend.domain.repository;

import com.templarios.backend.domain.model.AppUser;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<AppUser> findByUsername(String username);

    List<AppUser> findAll();

    boolean existsByUsername(String username);

    void save(AppUser user);
}
