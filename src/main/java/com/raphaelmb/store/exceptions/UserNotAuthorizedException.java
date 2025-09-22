package com.raphaelmb.store.exceptions;

public class UserNotAuthorizedException extends RuntimeException {
    public UserNotAuthorizedException() {
        super("User not authorized");
    }
}
