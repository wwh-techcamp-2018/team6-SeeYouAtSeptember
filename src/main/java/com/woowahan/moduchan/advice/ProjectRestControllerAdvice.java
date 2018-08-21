package com.woowahan.moduchan.advice;


import com.woowahan.moduchan.exception.ProjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ProjectRestControllerAdvice {
    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<Void> projectNotFound(ProjectNotFoundException exception) {
        log.debug("[ProjectNotFoundException] {}", exception.getMessage());
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
