package com.raphaelmb.store.services;

import com.raphaelmb.store.dtos.CreateProductRequest;
import com.raphaelmb.store.dtos.ProductDto;
import com.raphaelmb.store.dtos.UpdateProductRequest;
import com.raphaelmb.store.entities.Product;
import com.raphaelmb.store.exceptions.CategoryNotFoundException;
import com.raphaelmb.store.exceptions.ProductNotFoundException;
import com.raphaelmb.store.mappers.ProductMapper;
import com.raphaelmb.store.repositories.CategoryRepository;
import com.raphaelmb.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public List<ProductDto> getAllProducts(Byte categoryId) {
        List<Product> products;
        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        } else {
            products = productRepository.findAllWithCategory();
        }

        return products.stream().map(productMapper::toDto).toList();
    }

    public ProductDto getProductById(Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) throw new ProductNotFoundException();

        return productMapper.toDto(product);
    }

    public ProductDto createProduct(CreateProductRequest request) {
        var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if (category == null) throw new CategoryNotFoundException();

        var product = productMapper.toEntity(request);
        product.setCategory(category);
        productRepository.save(product);

        return productMapper.toDto(product);
    }

    public ProductDto updateProduct(Long id, UpdateProductRequest request) {
        var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        if (category == null) throw new CategoryNotFoundException();

        var product = productRepository.findById(id).orElse(null);
        if (product == null) throw new ProductNotFoundException();

        productMapper.update(request, product);
        product.setCategory(category);
        productRepository.save(product);

        return productMapper.toDto(product);
    }

    public void deleteProduct(Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) throw new ProductNotFoundException();

        productRepository.deleteById(id);
    }
}
