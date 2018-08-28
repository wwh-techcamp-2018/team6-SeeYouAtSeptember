package com.woowahan.moduchan.exception;

public class NotEnoughQuantityException extends RuntimeException {
    public NotEnoughQuantityException() {
        super("not enough quantity");
    }
}
