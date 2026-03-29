package com.elastic_app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elastic_app.dto.ProductView;
import com.elastic_app.entity.Product;
import com.elastic_app.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return service.save(product);
    }

    @GetMapping("/search")
    public List<Product> search(@RequestParam String q) {
        return service.advancedSearch(q);
    }

    @GetMapping("/all")
    public List<Product> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable String id) {
        Product product = service.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/product-name")
    public List<ProductView> findPartialData() {
        return service.findPartialData();
    }
}
