package com.woowahan.moduchan.controller;

import com.woowahan.moduchan.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> categoryNotFound(MethodArgumentNotValidException exception) {
        log.debug("[MethodArgumentNotValidException] {}", exception.getMessage());
        return new ResponseEntity(exception.getBindingResult().getAllErrors(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> loginUserNotFound(UnauthorizedException exception) {
        log.debug("[UnauthorizedException] {}", exception.getMessage());
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}
