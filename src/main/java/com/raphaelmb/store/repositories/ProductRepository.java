package com.raphaelmb.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raphaelmb.store.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}