package com.raphaelmb.store.services;

import com.raphaelmb.store.dtos.CheckoutRequest;
import com.raphaelmb.store.dtos.CheckoutResponse;
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

    public CheckoutResponse checkout(CheckoutRequest request) {
        var cart = cartService.getCartWithItems(request.getCartId());
        if (cart == null) throw new CartNotFoundException();

        if (cart.isEmpty()) throw new CartIsEmptyException();

        var order = Order.fromCart(cart, authService.getCurrentUser());

        orderService.saveOrder(order);

        cartService.clearCart(cart.getId());

        return new CheckoutResponse(order.getId());
    }
}
