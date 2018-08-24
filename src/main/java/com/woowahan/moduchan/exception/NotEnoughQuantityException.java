package com.woowahan.moduchan.exception;

public class NotEnoughQuantityException extends RuntimeException {
    public NotEnoughQuantityException() {
        super("");
    }

    public NotEnoughQuantityException(String message) {
        super(message);
    }
}
