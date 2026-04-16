package com.templarios.backend.domain.repository;

import com.templarios.backend.domain.model.Record;

import java.util.List;
import java.util.Optional;

public interface RecordRepository {

    List<Record> findAll();

    List<Record> findByOwnerUsername(String username);

    Optional<Record> findById(Long id);

    Record save(Record record);

    boolean deleteById(Long id);
}
