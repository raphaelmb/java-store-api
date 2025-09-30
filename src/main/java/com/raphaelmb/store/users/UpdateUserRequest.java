package com.raphaelmb.store.users;

import lombok.Data;

@Data
public class UpdateUserRequest {
    public String name;
    public String email;
}
