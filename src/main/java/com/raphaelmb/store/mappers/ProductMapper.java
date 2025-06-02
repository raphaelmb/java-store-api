package com.raphaelmb.store.mappers;

import com.raphaelmb.store.dtos.ProductDto;
import com.raphaelmb.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product product);
}
