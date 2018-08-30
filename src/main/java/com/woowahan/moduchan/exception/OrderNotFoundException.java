package com.woowahan.moduchan.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException() {
        super("order not found");
    }
}
