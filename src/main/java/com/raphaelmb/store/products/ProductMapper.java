package com.raphaelmb.store.products;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product product);

    Product toEntity(CreateProductRequest request);

    @Mapping(target = "id", ignore = true)
    void update(UpdateProductRequest request, @MappingTarget Product product);
}
