package com.woowahan.moduchan.domain;

import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.dto.user.UserDTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class NormalUserTest {

    private PasswordEncoder passwordEncoder;
    private NormalUser normalUser;

    @Before
    public void setUp() throws Exception {
        passwordEncoder = new BCryptPasswordEncoder();
        normalUser = NormalUser.from(new UserDTO(1L, "test@naver.com", "qwe123!!",
                "테스트", "010-1234-1234", "올림픽로 295")).encryptPassword(passwordEncoder);
    }

    @Test
    public void 비밀번호_확인_성공() {
        assertThat(normalUser.matchPassword("qwe123!!", passwordEncoder)).isEqualTo(true);
    }

    @Test
    public void 비밀번호_확인_실패() {
        assertThat(normalUser.matchPassword("qwe123!", passwordEncoder)).isEqualTo(false);
    }
}
