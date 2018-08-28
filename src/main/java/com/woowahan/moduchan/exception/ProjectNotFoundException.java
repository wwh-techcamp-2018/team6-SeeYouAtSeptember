package com.woowahan.moduchan.exception;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException() {
        super("project not found");
    }
}
