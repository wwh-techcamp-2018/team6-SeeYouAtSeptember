package com.woowahan.moduchan.interceptor;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiUserSessionInterceptor extends SessionInterceptor{
    @Override
    public void responseUnauth(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value());
    }
}
