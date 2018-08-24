package com.woowahan.moduchan.advice;

import com.woowahan.moduchan.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserRestControllerAdvice {

    @ExceptionHandler(UnAuthenticatedException.class)
    public ResponseEntity<Void> loginUserNotFound(UnAuthenticatedException exception) {
        log.debug("[UnAuthenticatedException] {}", exception.getMessage());
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<Void> unAuthorized(UnAuthorizedException exception) {
        log.debug("[UnAuthorizedException] {}", exception.getMessage());
        return new ResponseEntity(HttpStatus.FORBIDDEN);
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
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
