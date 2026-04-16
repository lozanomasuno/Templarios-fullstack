package com.templarios.backend.infrastructure.config;

import com.templarios.backend.domain.model.AppUser;
import com.templarios.backend.domain.model.Product;
import com.templarios.backend.domain.model.Role;
import com.templarios.backend.domain.repository.ProductRepository;
import com.templarios.backend.domain.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataSeeder {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      ProductRepository productRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void seed() {
        userRepository.save(new AppUser(1L, "admin", passwordEncoder.encode("Admin123*"), Role.ROLE_ADMIN));
        userRepository.save(new AppUser(2L, "user", passwordEncoder.encode("User123*"), Role.ROLE_USER));

        productRepository.save(new Product(null, "Ordenador", "Portatil de alto rendimiento", new BigDecimal("1299.99"), 8));
        productRepository.save(new Product(null, "Teclado", "Teclado mecanico", new BigDecimal("89.90"), 25));
    }
}
