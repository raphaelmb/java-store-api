package com.raphaelmb.store.mappers;

import com.raphaelmb.store.dtos.RegisterUserRequest;
import com.raphaelmb.store.dtos.UpdateUserRequest;
import com.raphaelmb.store.dtos.UserDto;
import com.raphaelmb.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void update(UpdateUserRequest request, @MappingTarget User user);
}
