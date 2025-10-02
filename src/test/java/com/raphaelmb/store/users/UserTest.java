package com.raphaelmb.store.users;

import static org.junit.jupiter.api.Assertions.*;

import com.raphaelmb.store.products.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("User")
class UserTest {
    private User user;
    private Product product;
    private Address address;

    @BeforeEach
    void setUp() {
        user = new User();
        address = new Address();
        product = new Product();
    }

    @Nested
    @DisplayName("Address Operations")
    class AddressOperations {

        @Test
        @DisplayName("Should add address and set bidirectional relationship")
        void shouldAddAddress() {
            user.addAddress(address);

            assertAll("Address should be added with bidirectional relationship",
                    () -> assertTrue(user.getAddresses().contains(address),
                            "User should contain the address"),
                    () -> assertEquals(user, address.getUser(),
                            "Address should reference the user")
            );
        }

        @Test
        @DisplayName("Should remove address and clear relationship")
        void shouldRemoveAddress() {
            user.addAddress(address);

            user.removeAddress(address);

            assertAll("Address should be removed and relationship cleared",
                    () -> assertFalse(user.getAddresses().contains(address),
                            "User should not contain the address"),
                    () -> assertNull(address.getUser(),
                            "Address should not reference the user")
            );
        }

        @Test
        @DisplayName("Should do nothing when removing non-existent address")
        void shouldDoNothingWhenRemovingNonExistentAddress() {
            Address otherAddress = new Address();

            user.removeAddress(otherAddress);

            assertEquals(0, user.getAddresses().size(),
                    "Address list should remain empty");
        }

        @Test
        @DisplayName("Should handle multiple addresses")
        void shouldHandleMultipleAddresses() {
            Address address2 = new Address();

            user.addAddress(address);
            user.addAddress(address2);

            assertAll("Multiple addresses should be managed correctly",
                    () -> assertEquals(2, user.getAddresses().size(),
                            "Should contain 2 addresses"),
                    () -> assertTrue(user.getAddresses().contains(address),
                            "Should contain first address"),
                    () -> assertTrue(user.getAddresses().contains(address2),
                            "Should contain second address")
            );
        }

        @Test
        @DisplayName("Should remove specific address from multiple addresses")
        void shouldRemoveSpecificAddress() {
            Address address2 = new Address();
            user.addAddress(address);
            user.addAddress(address2);

            user.removeAddress(address);

            assertAll("Only specified address should be removed",
                    () -> assertEquals(1, user.getAddresses().size(),
                            "Should have 1 address remaining"),
                    () -> assertFalse(user.getAddresses().contains(address),
                            "Removed address should not be in list"),
                    () -> assertTrue(user.getAddresses().contains(address2),
                            "Other address should remain in list")
            );
        }
    }

    @Nested
    @DisplayName("Favorite Products Operations")
    class FavoriteOperations {

        @Test
        @DisplayName("Should add product to favorites")
        void shouldAddFavoriteProduct() {
            user.addFavoriteProduct(product);

            assertTrue(user.getFavoriteProducts().contains(product),
                    "Favorites should contain the product");
        }

        @Test
        @DisplayName("Should not add duplicate products to favorites")
        void shouldNotAddDuplicates() {
            user.addFavoriteProduct(product);
            user.addFavoriteProduct(product);

            assertEquals(1, user.getFavoriteProducts().size(),
                    "Should contain only 1 product (no duplicates in Set)");
        }

        @Test
        @DisplayName("Should remove product from favorites")
        void shouldRemoveFavoriteProduct() {
            user.addFavoriteProduct(product);

            user.removeFavoriteProduct(product);

            assertFalse(user.getFavoriteProducts().contains(product),
                    "Product should be removed from favorites");
        }

        @Test
        @DisplayName("Should do nothing when removing non-existent favorite")
        void shouldDoNothingWhenRemovingNonExistentFavorite() {
            Product otherProduct = new Product();

            user.removeFavoriteProduct(otherProduct);

            assertEquals(0, user.getFavoriteProducts().size(),
                    "Favorites should remain empty");
        }

        @Test
        @DisplayName("Should handle multiple favorite products")
        void shouldHandleMultipleFavorites() {
            Product product2 = new Product();

            user.addFavoriteProduct(product);
            user.addFavoriteProduct(product2);

            assertAll("Multiple favorites should be managed correctly",
                    () -> assertEquals(2, user.getFavoriteProducts().size(),
                            "Should contain 2 favorite products"),
                    () -> assertTrue(user.getFavoriteProducts().contains(product),
                            "Should contain first product"),
                    () -> assertTrue(user.getFavoriteProducts().contains(product2),
                            "Should contain second product")
            );
        }

        @Test
        @DisplayName("Should remove specific product from multiple favorites")
        void shouldRemoveSpecificFavorite() {
            Product product2 = new Product();
            user.addFavoriteProduct(product);
            user.addFavoriteProduct(product2);

            user.removeFavoriteProduct(product);

            assertAll("Only specified product should be removed",
                    () -> assertEquals(1, user.getFavoriteProducts().size(),
                            "Should have 1 favorite remaining"),
                    () -> assertFalse(user.getFavoriteProducts().contains(product),
                            "Removed product should not be in favorites"),
                    () -> assertTrue(user.getFavoriteProducts().contains(product2),
                            "Other product should remain in favorites")
            );
        }
    }
}