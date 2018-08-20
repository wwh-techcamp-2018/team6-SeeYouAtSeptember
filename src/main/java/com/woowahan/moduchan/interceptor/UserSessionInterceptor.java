package com.woowahan.moduchan.interceptor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserSessionInterceptor extends SessionInterceptor {
    @Override
    public void responseUnauth(HttpServletResponse response) throws IOException {
        response.sendRedirect("/users/login");
    }
}
