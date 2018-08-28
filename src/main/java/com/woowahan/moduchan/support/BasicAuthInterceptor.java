package com.woowahan.moduchan.support;

import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.Base64;

@Slf4j
public class BasicAuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader("Authorization");
        log.debug("Authorization : {}", authorization);
        if (authorization == null || !authorization.startsWith("Basic")) {
            return true;
        }

        String base64Credentials = authorization.substring("Basic".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
        final String[] values = credentials.split(":", 2);
        log.debug("username : {}", values[0]);
        log.debug("password : {}", values[1]);

        UserDTO loginUserDTO = new UserDTO(null, values[0], values[1], null, null, null);
        log.debug("login user dto : {}", loginUserDTO);
        UserDTO userDTO = userService.login(loginUserDTO);
        log.debug("Login Success : {}", userDTO);
        request.getSession().setAttribute(SessionUtil.LOGIN_USER, userDTO);
        return true;
    }
}
