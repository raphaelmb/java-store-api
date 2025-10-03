package com.raphaelmb.store.orders;

import com.raphaelmb.store.carts.Cart;
import com.raphaelmb.store.payments.PaymentStatus;
import com.raphaelmb.store.products.Product;
import com.raphaelmb.store.users.User;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@DisplayName("Order")
class OrderTest {
    private User user1;
    private User user2;
    private Cart cart;
    private Product product;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);

        user2 = new User();
        user2.setId(2L);

        product = new Product();
        product.setId(1L);
        product.setPrice(new BigDecimal("10.00"));

        cart = createCartWithProduct(product, 2);
    }

    @Nested
    @DisplayName("isPlacedBy")
    class IsPlacedBy {

        @Test
        @DisplayName("Should return true when order is placed by the same customer")
        void shouldReturnTrueForSameCustomer() {
            Order order = new Order();
            order.setCustomer(user1);

            assertTrue(order.isPlacedBy(user1),
                    "Order should be placed by the same customer");
        }

        @Test
        @DisplayName("Should return false when order is placed by different customer")
        void shouldReturnFalseForDifferentCustomer() {
            Order order = new Order();
            order.setCustomer(user1);

            assertFalse(order.isPlacedBy(user2),
                    "Order should not be placed by different customer");
        }

        @Test
        @DisplayName("Should return false when customer is null")
        void shouldReturnFalseWhenCustomerIsNull() {
            Order order = new Order();
            order.setCustomer(user1);

            assertFalse(order.isPlacedBy(null),
                    "Should return false when comparing with null customer");
        }

        @Test
        @DisplayName("Should return false when order has no customer")
        void shouldReturnFalseWhenOrderHasNoCustomer() {
            Order order = new Order(); // customer not set

            assertFalse(order.isPlacedBy(user1),
                    "Should return false when order has no customer");
        }
    }

    @Nested
    @DisplayName("fromCart Factory Method")
    class FromCart {

        @Test
        @DisplayName("Should create order with correct customer")
        void shouldCreateOrderWithCustomer() {
            Order order = Order.fromCart(cart, user1);

            assertEquals(user1, order.getCustomer(),
                    "Order should have the correct customer");
        }

        @Test
        @DisplayName("Should create order with pending status")
        void shouldCreateOrderWithPendingStatus() {
            Order order = Order.fromCart(cart, user1);

            assertEquals(PaymentStatus.PENDING, order.getStatus(),
                    "Order should have PENDING status");
        }

        @Test
        @DisplayName("Should create order with correct total price from cart")
        void shouldCreateOrderWithCorrectTotalPrice() {
            Order order = Order.fromCart(cart, user1);

            assertEquals(new BigDecimal("20.00"), order.getTotalPrice(),
                    "Order total price should match cart total ($10 x 2 = $20)");
        }

        @Test
        @DisplayName("Should create order items from cart items")
        void shouldCreateOrderItemsFromCartItems() {
            Order order = Order.fromCart(cart, user1);

            assertAll("Order items should be created from cart",
                    () -> assertNotNull(order.getItems(),
                            "Order items should not be null"),
                    () -> assertEquals(1, order.getItems().size(),
                            "Order should have 1 item"),
                    () -> {
                        OrderItem orderItem = order.getItems().iterator().next();
                        assertEquals(product, orderItem.getProduct(),
                                "Order item should have the correct product");
                    },
                    () -> {
                        OrderItem orderItem = order.getItems().iterator().next();
                        assertEquals(2, orderItem.getQuantity(),
                                "Order item should have correct quantity");
                    }
            );
        }

        @Test
        @DisplayName("Should create order items with bidirectional relationship")
        void shouldCreateOrderItemsWithBidirectionalRelationship() {
            Order order = Order.fromCart(cart, user1);

            OrderItem orderItem = order.getItems().iterator().next();

            assertEquals(order, orderItem.getOrder(),
                    "Order item should reference the order");
        }

        @Test
        @DisplayName("Should handle cart with multiple products")
        void shouldHandleCartWithMultipleProducts() {
            Product product2 = new Product();
            product2.setId(2L);
            product2.setPrice(new BigDecimal("15.00"));

            cart.addItem(product2);

            Order order = Order.fromCart(cart, user1);

            assertAll("Order with multiple products",
                    () -> assertEquals(2, order.getItems().size(),
                            "Order should have 2 items"),
                    () -> assertEquals(new BigDecimal("35.00"), order.getTotalPrice(),
                            "Total should be $35 ($20 + $15)")
            );
        }

        @Test
        @DisplayName("Should handle empty cart")
        void shouldHandleEmptyCart() {
            Cart emptyCart = new Cart();

            Order order = Order.fromCart(emptyCart, user1);

            assertAll("Order from empty cart",
                    () -> assertEquals(BigDecimal.ZERO, order.getTotalPrice(),
                            "Total price should be zero for empty cart"),
                    () -> assertEquals(0, order.getItems().size(),
                            "Order should have no items")
            );
        }

        @Test
        @DisplayName("Should throw exception when cart is null")
        void shouldThrowExceptionWhenCartIsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> Order.fromCart(null, user1),
                    "Should throw exception when cart is null");
        }

        @Test
        @DisplayName("Should throw exception when customer is null")
        void shouldThrowExceptionWhenCustomerIsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> Order.fromCart(cart, null),
                    "Should throw exception when customer is null");
        }
    }

    private Cart createCartWithProduct(Product product, int quantity) {
        Cart cart = new Cart();
        for (int i = 0; i < quantity; i++) {
            cart.addItem(product);
        }
        return cart;
    }
}