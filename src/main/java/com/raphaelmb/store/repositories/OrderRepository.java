package com.raphaelmb.store.repositories;

import com.raphaelmb.store.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}
