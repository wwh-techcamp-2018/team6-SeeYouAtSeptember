package com.woowahan.moduchan.exception;

public class PasswordNotMatchedException extends RuntimeException {
    public PasswordNotMatchedException() {
        super("password not matched");
    }
}
