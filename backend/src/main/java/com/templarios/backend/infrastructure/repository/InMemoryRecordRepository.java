package com.templarios.backend.infrastructure.repository;

import com.templarios.backend.domain.model.Record;
import com.templarios.backend.domain.repository.RecordRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryRecordRepository implements RecordRepository {

    private final Map<Long, Record> records = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @Override
    public List<Record> findAll() {
        return records.values().stream()
                .sorted(Comparator.comparing(Record::getId).reversed())
                .toList();
    }

    @Override
    public List<Record> findByOwnerUsername(String username) {
        return records.values().stream()
                .filter(r -> r.getOwnerUsername().equals(username))
                .sorted(Comparator.comparing(Record::getId).reversed())
                .toList();
    }

    @Override
    public Optional<Record> findById(Long id) {
        return Optional.ofNullable(records.get(id));
    }

    @Override
    public Record save(Record record) {
        if (record.getId() == null) {
            record.setId(sequence.getAndIncrement());
        }
        records.put(record.getId(), record);
        return record;
    }

    @Override
    public boolean deleteById(Long id) {
        return records.remove(id) != null;
    }
}
