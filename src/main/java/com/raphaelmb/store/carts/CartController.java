package com.raphaelmb.store.carts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@AllArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/carts")
@Tag(name = "Carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    @Operation(summary = "", responses = {
            @ApiResponse()
    })
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder) {
        var cartDto = cartService.createCart();

        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();

        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    @Operation(summary = "Adds a product to the cart", responses = {
            @ApiResponse()
    })
    public ResponseEntity<CartItemDto> addToCart(
            @Parameter(description = "The ID of the cart") @PathVariable UUID cartId,
            @Valid @RequestBody AddItemToCartRequest request) {
        var cartItemDto = cartService.addToCart(cartId, request.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    @Operation(summary = "Gets cart by ID", responses = {
            @ApiResponse()
    })
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        var cartDto = cartService.getCart(cartId);

        return ResponseEntity.ok(cartDto);
    }

    @PutMapping("/{cartId}/items/{productId}")
    @Operation(summary = "Updates item from cart", responses = {
            @ApiResponse()
    })
    public ResponseEntity<CartItemDto> updateItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") @Positive Long productId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        var cartItemDto = cartService.updateItem(cartId, productId, request.getQuantity());

        return ResponseEntity.ok(cartItemDto);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    @Operation(summary = "Removes item from cart", responses = {
            @ApiResponse()
    })
    public ResponseEntity<Void> removeItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") @Positive Long productId) {
        cartService.removeItem(cartId, productId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    @Operation(summary = "Clears cart", responses = {
            @ApiResponse()
    })
    public ResponseEntity<Void> clearCart(@PathVariable("cartId") UUID cartId) {
        cartService.clearCart(cartId);

        return ResponseEntity.noContent().build();
    }
}
