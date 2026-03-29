package com.elastic_app.service;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import com.elastic_app.dto.ProductView;
import com.elastic_app.entity.Product;
import com.elastic_app.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final ElasticsearchOperations operations;

    public ProductService(ProductRepository repository, ElasticsearchOperations operations) {
        this.repository = repository;
        this.operations = operations;
    }

    // CRUD: Save
    public Product save(Product product) {
        return repository.save(product);
    }

    // CRUD: Find All
    public List<Product> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .toList();
    }

    // Advanced Search: Fuzzy + Weighted
    public List<Product> advancedSearch(String userInput) {
        Query query = NativeQuery.builder()
                .withQuery(q -> q
                        .multiMatch(m -> m
                                .fields("name^3") // Name is 3x more important
                                .query(userInput)
                                .fuzziness("AUTO") // Handles typos
                        ))
                .build();

        return operations.search(query, Product.class)
                .stream()
                .map(SearchHit::getContent)
                .toList();
    }

    public Product getProductById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public List<ProductView> findPartialData() {
        Iterable<Product> products = repository.findAll();
        
        return StreamSupport.stream(products.spliterator(), false)
                .map(p -> new ProductView(p.getName(), p.getPrice()))
                .toList();
    }
}
