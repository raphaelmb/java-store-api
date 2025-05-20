package com.raphaelmb.store.mappers;

import com.raphaelmb.store.dtos.UserDto;
import com.raphaelmb.store.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
