
# 🍲 Spring Boot + Elasticsearch: Food Delivery Search Service

This microservice provides high-performance CRUD and advanced search capabilities for a food delivery platform. It uses **Elasticsearch** as a specialized search store to handle fuzzy matching, weighted results, and real-time aggregations.

---

## 🏗️ Architecture Overview

In this setup, Elasticsearch acts as a secondary database optimized for queries.
* **Spring Boot:** Backend API and business logic.
* **Elasticsearch:** Distributed search engine for high-speed retrieval.
* **Kibana:** UI dashboard to visualize and manage Elasticsearch data.
* **Docker:** Orchestrates the infrastructure.

---

## 🚀 Getting Started

### 1. Prerequisites
* **Java 17** or higher
* **Maven**
* **Docker & Docker Compose**

### 2. Infrastructure Setup (Docker)
Create a `docker-compose.yml` in your project root and run `docker-compose up -d`.

```yaml
services:
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:8.12.0
    container_name: elasticsearch
    environment:
      - discovery.type=single-node
      - xpack.security.enabled=false
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ports:
      - 9200:9200
    networks:
      - elastic

  kibana:
    image: docker.elastic.co/kibana/kibana:8.12.0
    container_name: kibana
    ports:
      - 5601:5601
    environment:
      - ELASTICSEARCH_HOSTS=http://elasticsearch:9200
    networks:
      - elastic

networks:
  elastic:
    driver: bridge

🛠️ Application Configuration

Dependency (pom.xml)

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
</dependency>

Connection (application.properties)

spring.elasticsearch.uris=http://localhost:9200

📖 Key Implementations
1. Get Particular Document by ID
This implementation provides a direct lookup by document ID.
Endpoint: GET /api/products/{id}

// Service Method
public Product getById(String id) {
    return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
}

2. Advanced Fuzzy & Weighted Search
This logic allows users to find items even with typos (e.g., "Piza" -> "Pizza") and prioritizes matches in the product name over the description.

public List<Product> advancedSearch(String userInput) {
    Query query = NativeQuery.builder()
        .withQuery(q -> q
            .multiMatch(m -> m
                .fields("name^3", "description") // Name is 3x more important
                .query(userInput)
                .fuzziness("AUTO") // Handles typos
            )
        )
        .build();
    return operations.search(query, Product.class).stream().map(SearchHit::getContent).toList();
}

How to Verify with Kibana
Kibana is used to "peek" into the database for debugging purposes.

Open Kibana: Go to http://localhost:5601.

Dev Tools: Navigate to Management > Dev Tools.

Run Queries:

Check if data exists: GET /products/_search

Find by ID: GET /products/_doc/1

Check Index Mapping: GET /products/_mapping
