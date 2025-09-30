package com.raphaelmb.store.users;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    public String oldPassword;
    public String newPassword;
}
