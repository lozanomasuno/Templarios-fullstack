package com.templarios.backend.infrastructure.config;

import com.templarios.backend.domain.model.AppUser;
import com.templarios.backend.domain.model.Record;
import com.templarios.backend.domain.model.RecordStatus;
import com.templarios.backend.domain.model.Role;
import com.templarios.backend.domain.repository.RecordRepository;
import com.templarios.backend.domain.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataSeeder {

    private final UserRepository userRepository;
    private final RecordRepository recordRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      RecordRepository recordRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.recordRepository = recordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void seed() {
        userRepository.save(new AppUser(1L, "admin", passwordEncoder.encode("admin*"), Role.ROLE_ADMIN));
        userRepository.save(new AppUser(2L, "operario1", passwordEncoder.encode("Oper123*"), Role.ROLE_OPERATOR));
        userRepository.save(new AppUser(3L, "operario2", passwordEncoder.encode("Oper123*"), Role.ROLE_OPERATOR));

        LocalDateTime now = LocalDateTime.now();
        recordRepository.save(new Record(null, "Inspección inicial", "Revisión de equipos en sala A",
                RecordStatus.DONE, "operario1", now.minusDays(2), now.minusDays(1)));
        recordRepository.save(new Record(null, "Mantenimiento preventivo", "Cambio de filtros en unidad B",
                RecordStatus.IN_PROGRESS, "operario1", now.minusDays(1), now));
        recordRepository.save(new Record(null, "Reporte de incidencia", "Falla en sensor 3",
                RecordStatus.PENDING, "operario2", now, now));
    }
}
