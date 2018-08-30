package com.woowahan.moduchan.support;

import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.exception.UnAuthenticatedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SessionUtilTest {

    @Mock
    NativeWebRequest webRequest;

    @Test
    public void getLoginUserFromWebRequest_성공() {
        UserDTO userDTO = new UserDTO(null, "test@naver.com", "qwe123!!",
                "테스트", "01012341234", "올림픽로 295");
        when(webRequest.getAttribute(SessionUtil.LOGIN_USER, WebRequest.SCOPE_SESSION)).thenReturn(userDTO);
        UserDTO loginUserDTO = SessionUtil.getLoginUserFromWebRequest(webRequest);
        assertThat(loginUserDTO).isEqualTo(userDTO);
    }

    @Test(expected = UnAuthenticatedException.class)
    public void getLoginUserFromWebRequest_실패() {
        when(webRequest.getAttribute(SessionUtil.LOGIN_USER, WebRequest.SCOPE_SESSION)).thenReturn(null);
        UserDTO loginUserDTO = SessionUtil.getLoginUserFromWebRequest(webRequest);
    }
}