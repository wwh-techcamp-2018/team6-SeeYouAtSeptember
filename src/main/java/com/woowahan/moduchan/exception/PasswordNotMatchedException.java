package com.woowahan.moduchan.exception;

public class PasswordNotMatchedException extends RuntimeException {
    public PasswordNotMatchedException() {
        super("");
    }

    public PasswordNotMatchedException(String message) {
        super(message);
    }
}
