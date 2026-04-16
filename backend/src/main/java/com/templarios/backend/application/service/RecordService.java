package com.templarios.backend.application.service;

import com.templarios.backend.application.dto.RecordRequest;
import com.templarios.backend.application.dto.RecordResponse;

import java.util.List;

public interface RecordService {

    List<RecordResponse> findAllForCurrentUser(String username, boolean isAdmin);

    RecordResponse create(RecordRequest request, String ownerUsername);

    RecordResponse update(Long id, RecordRequest request, String requesterUsername, boolean isAdmin);

    void delete(Long id);
}
