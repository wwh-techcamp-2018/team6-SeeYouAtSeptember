package com.woowahan.moduchan.advice;

import com.woowahan.moduchan.exception.EmailAlreadyExistsException;
import com.woowahan.moduchan.exception.PasswordNotMatchedException;
import com.woowahan.moduchan.exception.UnAuthorizedException;
import com.woowahan.moduchan.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserRestControllerAdvice {

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<String> unAuthorized(UnAuthorizedException exception) {
        log.debug("[UnAuthorizedException] {}", exception.getMessage());
        return new ResponseEntity(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> userNotFound(UserNotFoundException exception) {
        log.debug("[UserNotFoundException] {}", exception.getMessage());
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> emailAlreadyExists(EmailAlreadyExistsException exception) {
        log.debug("[EmailAlreadyExistsException] {}", exception.getMessage());
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordNotMatchedException.class)
    public ResponseEntity<String> passwordNotMatched(PasswordNotMatchedException exception) {
        log.debug("[PasswordNotMatched] {}", exception.getMessage());
        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
