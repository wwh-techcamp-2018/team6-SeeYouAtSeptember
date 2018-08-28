package com.woowahan.moduchan.support;

import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.exception.UnAuthenticatedException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

public class SessionUtil {
    public static final String LOGIN_USER = "loginUser";

    public static UserDTO getLoginUserFromWebRequest(NativeWebRequest webRequest) {
        UserDTO loginUserDTO = (UserDTO) webRequest.getAttribute(LOGIN_USER, WebRequest.SCOPE_SESSION);
        // FIXME: 2018. 8. 20. guestUser 사용시 변경해야함(null->guestUser)
        if (loginUserDTO == null)
            throw new UnAuthenticatedException();
        return loginUserDTO;
    }
}
