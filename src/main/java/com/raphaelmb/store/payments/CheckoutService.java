package com.raphaelmb.store.payments;

import com.raphaelmb.store.auth.AuthService;
import com.raphaelmb.store.carts.CartService;
import com.raphaelmb.store.orders.Order;
import com.raphaelmb.store.carts.CartIsEmptyException;
import com.raphaelmb.store.carts.CartNotFoundException;
import com.raphaelmb.store.orders.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final AuthService authService;
    private final OrderService orderService;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;

    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {
        var cart = cartService.getCartWithItems(request.getCartId());
        if (cart == null) throw new CartNotFoundException();

        if (cart.isEmpty()) throw new CartIsEmptyException();

        var order = Order.fromCart(cart, authService.getCurrentUser());

        orderService.saveOrder(order);

        try {
            var session = paymentGateway.createCheckoutSession(order);

            cartService.clearCart(cart.getId());

            return new CheckoutResponse(order.getId(), session.getCheckoutUrl());
        } catch (PaymentException ex) {
            orderService.deleteOrder(order);
            throw ex;
        }
    }

    public void handleWebhookEvent(WebhookRequest request) {
        paymentGateway
                .parseWebhookRequest(request)
                .ifPresent(paymentResult -> {
                    var order = orderService.findById(paymentResult.getOrderId());
                    order.setStatus(paymentResult.getPaymentStatus());
                    orderService.saveOrder(order);
                });
    }
}
