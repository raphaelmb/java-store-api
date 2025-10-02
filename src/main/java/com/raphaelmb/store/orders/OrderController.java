package com.raphaelmb.store.orders;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
@Tag(name = "Orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "List orders", responses = {
            @ApiResponse()
    })
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        var result = orderService.getAllOrders();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Gets order by id", responses = {
            @ApiResponse()
    })
    public ResponseEntity<OrderDto> getOrder(@PathVariable(name = "orderId") @Positive Long orderId) {
        var result = orderService.getOrder(orderId);

        return ResponseEntity.ok(result);
    }

}
