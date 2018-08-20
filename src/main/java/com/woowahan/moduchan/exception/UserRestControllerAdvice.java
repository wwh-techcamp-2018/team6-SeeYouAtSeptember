package com.woowahan.moduchan.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserRestControllerAdvice {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Void> loginUserNotFound(UnauthorizedException exception) {
        log.debug("[UnauthorizedException] {}", exception.getMessage());
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> userNotFound(UserNotFoundException exception) {
        log.debug("[UserNotFoundException] {}", exception.getMessage());
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Void> emailAlreadyExists(EmailAlreadyExistsException exception) {
        log.debug("[EmailAlreadyExistsException] {}", exception.getMessage());
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordNotMatchedException.class)
    public ResponseEntity<Void> passwordNotMatched(PasswordNotMatchedException exception) {
        log.debug("[PasswordNotMatched] {}", exception.getMessage());
        // TODO: 2018. 8. 21. BAD_REQUEST VS UNAUTHORIZED
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}
