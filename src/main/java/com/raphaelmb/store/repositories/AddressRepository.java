package com.raphaelmb.store.repositories;

import org.springframework.data.repository.CrudRepository;

import com.raphaelmb.store.entities.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {
}