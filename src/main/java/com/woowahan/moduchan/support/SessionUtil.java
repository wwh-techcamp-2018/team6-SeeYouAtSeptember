package com.woowahan.moduchan.support;

import com.woowahan.moduchan.dto.UserDTO;

import javax.servlet.http.HttpSession;

public class SessionUtil {
    private static final String LOGIN_USER = "loginUser";

    public static UserDTO getLoginUser(HttpSession session) {
        return (UserDTO) session.getAttribute(LOGIN_USER);
    }

    public static HttpSession setLoginUser(HttpSession session, UserDTO userDTO) {
        session.setAttribute(LOGIN_USER, userDTO);
        return session;
    }

    public static HttpSession resetLoginUser(HttpSession session) {
        session.removeAttribute(LOGIN_USER);
        return session;
    }
}
