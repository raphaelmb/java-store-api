package com.raphaelmb.store.payments;

import com.raphaelmb.store.orders.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/checkout")
@Tag(name = "Checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<CheckoutResponse> checkout(@Valid @RequestBody CheckoutRequest request) {
            var checkout = checkoutService.checkout(request);
            return ResponseEntity.ok(checkout);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
            @RequestHeader Map<String, String> headers,
            @RequestBody String payload
    ) {
        checkoutService.handleWebhookEvent(new WebhookRequest(headers, payload));

        return ResponseEntity.ok().build();
    }
}
