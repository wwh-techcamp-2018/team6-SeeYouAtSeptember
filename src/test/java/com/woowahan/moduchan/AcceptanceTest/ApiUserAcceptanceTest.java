package com.woowahan.moduchan.AcceptanceTest;

import com.woowahan.moduchan.dto.user.UserDTO;
import util.ApiAcceptanceTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiUserAcceptanceTest extends ApiAcceptanceTest {
    private UserDTO loginUserDTO;
    private UserDTO joinUserDTO;

    @Before
    public void setUp() throws Exception {
        loginUserDTO = new UserDTO(null, "a", "a", null, null, null);
        joinUserDTO = getDefaultUserDTO();
    }

    @Test
    public void 로그인_성공() {
        ResponseEntity<Void> response = template().postForEntity("/api/users/login", loginUserDTO, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void 로그인_실패_없는_이메일() {
        loginUserDTO.setEmail("b");
        ResponseEntity<String> response = template().postForEntity("/api/users/login", loginUserDTO, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().contains("user not found")).isTrue();
    }

    @Test
    public void 로그인_실패_틀린_비밀번호() {
        loginUserDTO.setPassword("b");
        ResponseEntity<String> response = template().postForEntity("/api/users/login", loginUserDTO, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().contains("password not matched")).isTrue();
    }

    @Test
    public void 회원가입_성공() {
        ResponseEntity<Void> response = template().postForEntity("/api/users", joinUserDTO, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void 회원가입_실패_이미_존재하는_이메일() {
        joinUserDTO.setEmail("bgrigoriant@bandcamp.com");
        ResponseEntity<String> response = template().postForEntity("/api/users", joinUserDTO, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().contains("email already exists")).isTrue();
    }

    @Test
    public void 로그아웃_성공() {
        ResponseEntity<Void> response = templateWithNormalUser().postForEntity("/api/users/logout", null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void 로그아웃_실패() {
        ResponseEntity<Void> response = template().postForEntity("/api/users/logout", null, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
