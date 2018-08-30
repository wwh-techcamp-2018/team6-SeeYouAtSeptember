package com.woowahan.moduchan.advice;


import com.woowahan.moduchan.exception.NotEnoughQuantityException;
import com.woowahan.moduchan.exception.OrderNotFoundException;
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
    public ResponseEntity<String> productNotFound(ProductNotFoundException exception) {
        log.debug("[ProductNotFoundException] {}", exception.getMessage());
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotEnoughQuantityException.class)
    public ResponseEntity<String> notEnoughQuantity(NotEnoughQuantityException exception) {
        log.debug("[NotEnoughQuantityException] {}", exception.getMessage());
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> notFoundOrder(OrderNotFoundException exception) {
        log.debug("[OrderNotFoundException] {}", exception.getMessage());
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
