package com.raphaelmb.store.services;

import com.raphaelmb.store.orders.Order;
import com.raphaelmb.store.orders.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
}
