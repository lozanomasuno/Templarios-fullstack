package com.templarios.backend.application.service.impl;

import com.templarios.backend.application.dto.RecordRequest;
import com.templarios.backend.application.dto.RecordResponse;
import com.templarios.backend.application.service.RecordService;
import com.templarios.backend.domain.model.Record;
import com.templarios.backend.domain.model.RecordStatus;
import com.templarios.backend.domain.repository.RecordRepository;
import com.templarios.backend.shared.exception.ResourceNotFoundException;
import com.templarios.backend.shared.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;

    public RecordServiceImpl(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public List<RecordResponse> findAllForCurrentUser(String username, boolean isAdmin) {
        List<Record> records = isAdmin
                ? recordRepository.findAll()
                : recordRepository.findByOwnerUsername(username);
        return records.stream().map(this::toResponse).toList();
    }

    @Override
    public RecordResponse create(RecordRequest request, String ownerUsername) {
        Record record = new Record(
                null,
                request.getTitle(),
                request.getDescription(),
                request.getStatus() != null ? request.getStatus() : RecordStatus.PENDING,
                ownerUsername,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        return toResponse(recordRepository.save(record));
    }

    @Override
    public RecordResponse update(Long id, RecordRequest request, String requesterUsername, boolean isAdmin) {
        Record existing = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro no encontrado: " + id));

        if (!isAdmin && !existing.getOwnerUsername().equals(requesterUsername)) {
            throw new UnauthorizedException("No tienes permiso para modificar este registro");
        }

        existing.setTitle(request.getTitle());
        existing.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }
        existing.setUpdatedAt(LocalDateTime.now());

        return toResponse(recordRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        if (!recordRepository.deleteById(id)) {
            throw new ResourceNotFoundException("Registro no encontrado: " + id);
        }
    }

    private RecordResponse toResponse(Record r) {
        return new RecordResponse(
                r.getId(), r.getTitle(), r.getDescription(),
                r.getStatus(), r.getOwnerUsername(),
                r.getCreatedAt(), r.getUpdatedAt()
        );
    }
}
