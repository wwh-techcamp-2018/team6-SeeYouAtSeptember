package com.woowahan.moduchan.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException() {
        super("category not found");
    }
}
