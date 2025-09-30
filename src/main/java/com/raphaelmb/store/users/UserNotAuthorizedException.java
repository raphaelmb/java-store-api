package com.raphaelmb.store.users;

public class UserNotAuthorizedException extends RuntimeException {
    public UserNotAuthorizedException() {
        super("User not authorized");
    }
}
