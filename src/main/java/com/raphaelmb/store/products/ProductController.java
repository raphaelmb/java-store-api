package com.raphaelmb.store.products;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/v1/products")
@Tag(name = "Products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Gets all products")
    @ApiResponses({
            @ApiResponse(responseCode = "200")
    })
    public ResponseEntity<List<ProductDto>> getAllProducts(@RequestParam(required = false, name = "categoryId") Byte categoryId) {
        var products = productService.getAllProducts(categoryId);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")
    })
    public ResponseEntity<ProductDto> getProduct(@PathVariable @Positive Long id) {
        var product = productService.getProductById(id);

        return ResponseEntity.ok(product);
    }

    @PostMapping
    @Operation(summary = "Creates a new product")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
    })
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody CreateProductRequest request, UriComponentsBuilder uriBuilder) {
        var productDto = productService.createProduct(request);

        var uri = uriBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();

        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates a product")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")
    })
    public ResponseEntity<ProductDto> updateProduct(@PathVariable(name = "id") @Positive Long id, @Valid @RequestBody UpdateProductRequest request) {
        var productDto = productService.updateProduct(id, request);

        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a product")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404")
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") @Positive Long id) {
        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}
