package com.woowahan.moduchan.interceptor;

import com.woowahan.moduchan.dto.UserDTO;
import com.woowahan.moduchan.support.SessionUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class SessionInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserDTO userDTO = (UserDTO)request.getSession().getAttribute(SessionUtil.LOGIN_USER);
        // TODO: 2018. 8. 19. guestUser 생성시 변경해야함
        if(userDTO == null){
            responseUnauth(response);
            return false;
        }
        // TODO: 2018. 8. 19. adminUser 생성시 추가해야함
        return true;
    }

    public abstract void responseUnauth(HttpServletResponse response) throws IOException;
}