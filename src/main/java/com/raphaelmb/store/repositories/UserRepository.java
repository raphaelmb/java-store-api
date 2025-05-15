package com.raphaelmb.store.repositories;

import org.springframework.data.repository.CrudRepository;

import com.raphaelmb.store.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
