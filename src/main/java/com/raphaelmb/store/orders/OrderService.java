package com.raphaelmb.store.orders;

import com.raphaelmb.store.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final OrderMapper orderMapper;

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public List<OrderDto> getAllOrders() {
        var user = authService.getCurrentUser();
        var orders = orderRepository.getOrdersByCustomer(user);

        return orders.stream().map(orderMapper::toDto).toList();
    }

    public OrderDto getOrder(Long orderId) {
        var order = orderRepository.getOrderWithItems(orderId).orElseThrow(OrderNotFoundException::new);

        var user = authService.getCurrentUser();
        if (!order.isPlacedBy(user)) throw new AccessDeniedException("You don't have access to this order");

        return orderMapper.toDto(order);
    }

    public void deleteOrder(Order order) {
        orderRepository.delete(order);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }
}
