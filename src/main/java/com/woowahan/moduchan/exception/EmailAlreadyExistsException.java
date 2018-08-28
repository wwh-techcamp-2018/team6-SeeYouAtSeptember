package com.woowahan.moduchan.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("email already exists");
    }
}
