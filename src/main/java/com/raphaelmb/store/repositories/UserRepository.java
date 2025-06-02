package com.raphaelmb.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raphaelmb.store.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
