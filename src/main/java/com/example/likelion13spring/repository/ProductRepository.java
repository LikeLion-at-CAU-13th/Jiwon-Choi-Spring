package com.example.likelion13spring.repository;

import com.example.likelion13spring.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}