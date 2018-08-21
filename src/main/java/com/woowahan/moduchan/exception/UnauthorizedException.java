package com.woowahan.moduchan.exception;

public class UnAuthorizedException extends RuntimeException {
    public UnAuthorizedException() {
        super("");
    }

    public UnAuthorizedException(String message) {
        super(message);
    }
}
