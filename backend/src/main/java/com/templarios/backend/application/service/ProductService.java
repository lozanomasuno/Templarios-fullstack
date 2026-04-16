package com.templarios.backend.application.service;

import com.templarios.backend.application.dto.ProductRequest;
import com.templarios.backend.application.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    List<ProductResponse> getAllProducts();

    ProductResponse createProduct(ProductRequest request);
}
