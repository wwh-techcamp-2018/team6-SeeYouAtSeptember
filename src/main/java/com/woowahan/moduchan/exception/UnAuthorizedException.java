package com.woowahan.moduchan.exception;

public class UnAuthorizedException extends RuntimeException {
    public UnAuthorizedException() {
        super("unauthorized");
    }
}
