package com.woowahan.moduchan.web;

import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.support.ApiAcceptanceTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiUserControllerTest extends ApiAcceptanceTest {

    private UserDTO loginUserDTO;
    private UserDTO joinUserDTO;

    @Before
    public void setUp() throws Exception {
        loginUserDTO = new UserDTO(null,"a", "a",null,null,null);
        joinUserDTO = new UserDTO(null,"chldbtjd@naver.com","qwe123@@",
                "최유성","01035442272","강남강남가아아아남");

    }

    @Test
    public void 로그인_성공() {
        ResponseEntity<Void> response = template().postForEntity("/api/users/login", loginUserDTO, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    public void 회원가입_성공() {
        ResponseEntity<Void> response = template().postForEntity("/api/users", joinUserDTO, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
