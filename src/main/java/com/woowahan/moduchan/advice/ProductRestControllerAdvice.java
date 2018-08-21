package com.woowahan.moduchan.advice;


import com.woowahan.moduchan.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ProductRestControllerAdvice {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Void> productNotFound(ProductNotFoundException exception) {
        log.debug("[ProductNotFoundException] {}", exception.getMessage());
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
