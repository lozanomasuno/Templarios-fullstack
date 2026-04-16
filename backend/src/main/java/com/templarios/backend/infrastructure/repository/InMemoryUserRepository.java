package com.templarios.backend.infrastructure.repository;

import com.templarios.backend.domain.model.AppUser;
import com.templarios.backend.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<String, AppUser> users = new ConcurrentHashMap<>();

    @Override
    public Optional<AppUser> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    @Override
    public void save(AppUser user) {
        users.put(user.getUsername(), user);
    }
}
