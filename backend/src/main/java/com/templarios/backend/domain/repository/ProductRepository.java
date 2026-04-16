package com.templarios.backend.domain.repository;

import com.templarios.backend.domain.model.Product;

import java.util.List;

public interface ProductRepository {

    List<Product> findAll();

    Product save(Product product);
}
