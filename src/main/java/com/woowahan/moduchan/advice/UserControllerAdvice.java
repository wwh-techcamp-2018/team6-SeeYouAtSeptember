package com.woowahan.moduchan.advice;

import com.woowahan.moduchan.exception.UnAuthenticatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(UnAuthenticatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String loginUserNotFound(UnAuthenticatedException exception) {
        log.debug("[UnAuthenticatedException] {}", exception.getMessage());
        return "/login";
    }
}
