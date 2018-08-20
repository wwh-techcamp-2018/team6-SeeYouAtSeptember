package com.woowahan.moduchan.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("unauthorized exception");
    }
}
