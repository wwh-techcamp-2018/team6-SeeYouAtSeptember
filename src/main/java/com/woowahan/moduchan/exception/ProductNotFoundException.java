package com.woowahan.moduchan.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
        super("product not found");
    }
}
