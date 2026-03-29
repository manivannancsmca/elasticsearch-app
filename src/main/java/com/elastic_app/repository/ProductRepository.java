package com.elastic_app.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.elastic_app.dto.ProductView;
import com.elastic_app.entity.Product;

public interface ProductRepository extends ElasticsearchRepository<Product, String> {
    List<Product> findByNameContainingIgnoreCase(String name);

    List<ProductView> findAllProjectedBy();
}
