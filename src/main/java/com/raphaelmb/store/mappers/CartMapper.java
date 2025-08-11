package com.raphaelmb.store.mappers;

import com.raphaelmb.store.dtos.CartDto;
import com.raphaelmb.store.dtos.CartItemDto;
import com.raphaelmb.store.entities.Cart;
import com.raphaelmb.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
