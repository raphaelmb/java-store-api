package com.raphaelmb.store.controllers;

import com.raphaelmb.store.dtos.CheckoutRequest;
import com.raphaelmb.store.dtos.CheckoutResponse;
import com.raphaelmb.store.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<CheckoutResponse> checkout(@Valid @RequestBody CheckoutRequest request) {
        var orderId = checkoutService.getCartWithItems(request.getCartId());

        return ResponseEntity.ok(new CheckoutResponse(orderId));
    }
}
