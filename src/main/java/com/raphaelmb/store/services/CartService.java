package com.raphaelmb.store.services;

import com.raphaelmb.store.dtos.CartDto;
import com.raphaelmb.store.dtos.CartItemDto;
import com.raphaelmb.store.entities.Cart;
import com.raphaelmb.store.exceptions.CartNotFoundException;
import com.raphaelmb.store.exceptions.ProductNotFoundException;
import com.raphaelmb.store.mappers.CartMapper;
import com.raphaelmb.store.repositories.CartRepository;
import com.raphaelmb.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private ProductRepository productRepository;

    public CartDto createCart() {
        var cart = new Cart();
        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    public CartItemDto addToCart(UUID cartId, Long productId) {
        var cart = cartRepository.getCartWithItems(cartId).orElseThrow(CartNotFoundException::new);
        // if (cart == null) throw new CartNotFoundException();

        var product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        // if (product == null) throw new ProductNotFoundException();

        var cartItem = cart.addItem(product);

        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElseThrow(CartNotFoundException::new);
        //if (cart == null) throw new CartNotFoundException();

        return cartMapper.toDto(cart);
    }

    public CartItemDto updateItem(UUID cartId, Long productId, Integer quantity) {
        var cart = cartRepository.getCartWithItems(cartId).orElseThrow(CartNotFoundException::new);
        // if (cart == null) throw new CartNotFoundException();

        var cartItem = cart.getItem(productId);
        if (cartItem == null) throw new ProductNotFoundException();

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public void removeItem(UUID cartId, Long productId) {
        var cart = cartRepository.getCartWithItems(cartId).orElseThrow(CartNotFoundException::new);

        cart.removeItem(productId);

        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElseThrow(CartNotFoundException::new);

        cart.clear();

        cartRepository.save(cart);
    }

    public Cart getCartWithItems(UUID id) {
        return cartRepository.getCartWithItems(id).orElse(null);
    }
}
