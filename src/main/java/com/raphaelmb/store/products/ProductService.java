package com.raphaelmb.store.products;

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
        var product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        return productMapper.toDto(product);
    }

    public ProductDto createProduct(CreateProductRequest request) {
        var category = categoryRepository.findById(request.getCategoryId()).orElseThrow(CategoryNotFoundException::new);

        var product = productMapper.toEntity(request);
        product.setCategory(category);
        productRepository.save(product);

        return productMapper.toDto(product);
    }

    public ProductDto updateProduct(Long id, UpdateProductRequest request) {
        var category = categoryRepository.findById(request.getCategoryId()).orElseThrow(CategoryNotFoundException::new);

        var product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        productMapper.update(request, product);
        product.setCategory(category);
        productRepository.save(product);

        return productMapper.toDto(product);
    }

    public void deleteProduct(Long id) {
        productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        productRepository.deleteById(id);
    }
}
