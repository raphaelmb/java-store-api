package com.raphaelmb.store.controllers;

import com.raphaelmb.store.dtos.CreateProductRequest;
import com.raphaelmb.store.dtos.ProductDto;
import com.raphaelmb.store.dtos.UpdateProductRequest;
import com.raphaelmb.store.exceptions.CategoryNotFoundException;
import com.raphaelmb.store.exceptions.ProductNotFoundException;
import com.raphaelmb.store.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
@Tag(name = "Products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Gets all products")
    public List<ProductDto> getAllProducts(@RequestParam(required = false, name = "categoryId") Byte categoryId) {
        return productService.getAllProducts(categoryId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Gets product by ID")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        var product = productService.getProductById(id);

        return ResponseEntity.ok(product);
    }

    @PostMapping
    @Operation(summary = "Creates a new product")
    public ResponseEntity<ProductDto> createProduct(@RequestBody CreateProductRequest request, UriComponentsBuilder uriBuilder) {
        var productDto = productService.createProduct(request);

        var uri = uriBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();

        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates a product")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable(name = "id") Long id, @RequestBody UpdateProductRequest request) {
        var productDto = productService.updateProduct(id, request);

        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a product")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") Long id) {
        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCategoryNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Category not found"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Product not found"));
    }
}
