package com.raphaelmb.store.repositories;

import org.springframework.data.repository.CrudRepository;

import com.raphaelmb.store.entities.Category;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}