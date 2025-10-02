package com.raphaelmb.store.carts;

import static org.junit.jupiter.api.Assertions.*;

import com.raphaelmb.store.products.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@DisplayName("Cart")
class CartTest {
    private Cart cart;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        cart = new Cart();

        product1 = new Product();
        product1.setId(1L);
        product1.setPrice(new BigDecimal("10.00"));

        product2 = new Product();
        product2.setId(2L);
        product2.setPrice(new BigDecimal("20.00"));
    }

    @Nested
    @DisplayName("Cart State")
    class CartState {
        @Test
        @DisplayName("Should be empty when no items added")
        void shouldBeEmpty() {
            assertTrue(cart.isEmpty(), "Cart should be empty initially");
        }

        @Test
        @DisplayName("Should not be empty after adding items")
        void shouldNotBeEmpty() {
            cart.addItem(product1);

            assertFalse(cart.isEmpty(), "Cart should not be empty after adding item");
        }
    }

    @Nested
    @DisplayName("Clearing Cart")
    class ClearCart {
        @Test
        @DisplayName("Should remove all items from cart")
        void shouldClearItems() {
            cart.addItem(product1);
            cart.addItem(product2);

            cart.clear();

            assertAll("Cart should be empty after clearing",
                    () -> assertEquals(0, cart.getItems().size(), "Items size should be 0"),
                    () -> assertTrue(cart.isEmpty(), "isEmpty should return true")
            );
        }
    }

    @Nested
    @DisplayName("Adding Items")
    class AddItem {
        @Test
        @DisplayName("Should add new item to cart")
        void shouldAddItem() {
            cart.addItem(product1);

            assertEquals(1, cart.getItems().size(), "Cart should contain 1 item");
        }

        @Test
        @DisplayName("Should increment quantity when adding same product")
        void shouldIncrementQuantityWhenProductAlreadyExists() {
            cart.addItem(product1);
            cart.addItem(product1);

            assertAll("Same product added twice",
                    () -> assertEquals(1, cart.getItems().size(), "Should contain only 1 unique item"),
                    () -> assertEquals(2, cart.getItem(1L).getQuantity(), "Quantity should be 2")
            );
        }
    }

    @Nested
    @DisplayName("Removing Items")
    class RemoveItem {
        @Test
        @DisplayName("Should remove existing item from cart")
        void shouldRemoveItem() {
            cart.addItem(product1);

            cart.removeItem(product1.getId());

            assertEquals(0, cart.getItems().size(), "Cart should be empty after removing item");
        }

        @Test
        @DisplayName("Should do nothing when removing non-existent product")
        void shouldDoNothingWhenProductNotInCart() {
            cart.removeItem(999L);

            assertEquals(0, cart.getItems().size(), "Cart should remain empty");
        }

        @Test
        @DisplayName("Should clear bidirectional relationship when removing item")
        void shouldClearBidirectionalRelationship() {
            CartItem item = cart.addItem(product1);

            cart.removeItem(product1.getId());

            assertNull(item.getCart(), "Item's cart reference should be null after removal");
        }
    }

    @Nested
    @DisplayName("Getting Items")
    class GetItem {
        @Test
        @DisplayName("Should retrieve item by product ID")
        void shouldGetItem() {
            cart.addItem(product1);

            CartItem item = cart.getItem(product1.getId());

            assertNotNull(item, "Item should not be null");
            assertEquals(product1.getId(), item.getProduct().getId(), "Product IDs should match");
        }

        @Test
        @DisplayName("Should return null when product not found")
        void shouldReturnNullWhenProductNotFound() {
            CartItem item = cart.getItem(999L);

            assertNull(item, "Should return null for non-existent product");
        }
    }

    @Nested
    @DisplayName("Calculating Total Price")
    class GetTotalPrice {
        @Test
        @DisplayName("Should return zero for empty cart")
        void shouldReturnZeroForEmptyCart() {
            assertEquals(BigDecimal.ZERO, cart.getTotalPrice(), "Empty cart total should be zero");
        }

        @Test
        @DisplayName("Should calculate total for multiple different products")
        void shouldCalculateTotalCorrectly() {
            cart.addItem(product1);
            cart.addItem(product2);

            assertEquals(new BigDecimal("30.00"), cart.getTotalPrice(), "Total should be $30.00");
        }

        @Test
        @DisplayName("Should calculate total with product quantity")
        void shouldCalculateTotalWithQuantity() {
            cart.addItem(product1);
            cart.addItem(product1);

            assertEquals(new BigDecimal("20.00"), cart.getTotalPrice(), "Total should be $20.00");
        }
    }
}
