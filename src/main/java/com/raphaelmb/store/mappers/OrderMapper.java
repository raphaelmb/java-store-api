package com.raphaelmb.store.mappers;

import com.raphaelmb.store.dtos.OrderDto;
import com.raphaelmb.store.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
