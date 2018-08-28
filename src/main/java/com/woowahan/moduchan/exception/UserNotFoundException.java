package com.woowahan.moduchan.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("user not found");
    }
}
