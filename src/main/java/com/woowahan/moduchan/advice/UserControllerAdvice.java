package com.woowahan.moduchan.advice;

import com.woowahan.moduchan.exception.UnAuthenticatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(UnAuthenticatedException.class)
    public String loginUserNotFound(UnAuthenticatedException exception) {
        log.debug("[UnAuthenticatedException] {}", exception.getMessage());
        return "/login";
    }
}
