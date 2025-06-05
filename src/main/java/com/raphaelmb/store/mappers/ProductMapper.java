package com.raphaelmb.store.mappers;

import com.raphaelmb.store.dtos.CreateProductRequest;
import com.raphaelmb.store.dtos.ProductDto;
import com.raphaelmb.store.dtos.UpdateProductRequest;
import com.raphaelmb.store.entities.Product;
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
