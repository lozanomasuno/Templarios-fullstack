package com.templarios.backend.infrastructure.repository;

import com.templarios.backend.domain.model.Product;
import com.templarios.backend.domain.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    private final Map<Long, Product> products = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    @Override
    public List<Product> findAll() {
        return products.values().stream()
                .sorted(Comparator.comparing(Product::getId))
                .toList();
    }

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(sequence.getAndIncrement());
        }
        products.put(product.getId(), product);
        return product;
    }
}
