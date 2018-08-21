package com.woowahan.moduchan.exception;

public class UnAuthenticatedException extends RuntimeException {
    public UnAuthenticatedException() {
        super("");
    }

    public UnAuthenticatedException(String message) {
        super(message);
    }
}
