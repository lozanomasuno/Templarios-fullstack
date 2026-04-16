package com.templarios.backend.infrastructure.repository;

import com.templarios.backend.domain.model.AppUser;
import com.templarios.backend.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<String, AppUser> users = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @Override
    public Optional<AppUser> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    @Override
    public List<AppUser> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean existsByUsername(String username) {
        return users.containsKey(username);
    }

    @Override
    public void save(AppUser user) {
        if (user.getId() == null) {
            user.setId(sequence.getAndIncrement());
        } else {
            sequence.accumulateAndGet(user.getId() + 1, Math::max);
        }
        users.put(user.getUsername(), user);
    }
}
