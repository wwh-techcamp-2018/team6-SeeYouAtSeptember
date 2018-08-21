package com.woowahan.moduchan.exception;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException() {
        super("");
    }

    public ProjectNotFoundException(String message) {
        super(message);
    }
}
