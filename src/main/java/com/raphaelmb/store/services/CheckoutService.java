package com.raphaelmb.store.services;

import com.raphaelmb.store.entities.Cart;
import com.raphaelmb.store.entities.Order;
import com.raphaelmb.store.entities.OrderItem;
import com.raphaelmb.store.entities.OrderStatus;
import com.raphaelmb.store.exceptions.CartIsEmptyException;
import com.raphaelmb.store.exceptions.CartNotFoundException;
import com.raphaelmb.store.repositories.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CheckoutService {
    private final AuthService authService;
    private final OrderService orderService;
    private final CartService cartService;

    public Long getCartWithItems(UUID cartId) {
        var cart = cartService.getCartWithItems(cartId);
        if (cart == null) throw new CartNotFoundException();

        if (cart.getItems().isEmpty()) throw new CartIsEmptyException();

        var order = new Order();
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(authService.getCurrentUser());

        cart.getItems().forEach(item -> {
            var orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalPrice(item.getTotalPrice());
            orderItem.setUnitPrice(item.getProduct().getPrice());
            order.getItems().add(orderItem);
        });

        orderService.saveOrder(order);

        cartService.clearCart(cart.getId());

        return order.getId();
    }
}
