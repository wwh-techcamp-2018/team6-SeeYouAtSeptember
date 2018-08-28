package com.woowahan.moduchan.advice;

import com.woowahan.moduchan.exception.CategoryNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CategoryRestControllerAdvice {
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> categoryNotFound(CategoryNotFoundException exception) {
        log.debug("[CategoryNotFoundException] {}", exception.getMessage());
        return new ResponseEntity("category not found",HttpStatus.BAD_REQUEST);
    }
}
