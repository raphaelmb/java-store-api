package com.raphaelmb.store.carts;

public class CartIsEmptyException extends RuntimeException {
    public CartIsEmptyException() {
        super("Cart is empty");
    }
}
