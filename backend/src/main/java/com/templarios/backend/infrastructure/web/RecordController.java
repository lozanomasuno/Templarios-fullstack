package com.templarios.backend.infrastructure.web;

import com.templarios.backend.application.dto.RecordRequest;
import com.templarios.backend.application.dto.RecordResponse;
import com.templarios.backend.application.service.RecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping
    public ResponseEntity<List<RecordResponse>> getAll(Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(recordService.findAllForCurrentUser(auth.getName(), isAdmin));
    }

    @PostMapping
    public ResponseEntity<RecordResponse> create(@Valid @RequestBody RecordRequest request,
                                                  Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recordService.create(request, auth.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecordResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody RecordRequest request,
                                                  Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(recordService.update(id, request, auth.getName(), isAdmin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recordService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
