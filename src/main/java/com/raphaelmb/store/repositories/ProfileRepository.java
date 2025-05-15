package com.raphaelmb.store.repositories;

import org.springframework.data.repository.CrudRepository;

import com.raphaelmb.store.entities.Profile;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}