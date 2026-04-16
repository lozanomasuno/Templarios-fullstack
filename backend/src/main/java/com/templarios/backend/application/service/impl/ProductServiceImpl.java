package com.templarios.backend.application.service.impl;

import com.templarios.backend.application.dto.ProductRequest;
import com.templarios.backend.application.dto.ProductResponse;
import com.templarios.backend.application.service.ProductService;
import com.templarios.backend.domain.model.Product;
import com.templarios.backend.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product(
                null,
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock()
        );
        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock()
        );
    }
}
