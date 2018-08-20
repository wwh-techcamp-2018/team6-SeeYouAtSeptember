package com.woowahan.moduchan.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("");
    }

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
