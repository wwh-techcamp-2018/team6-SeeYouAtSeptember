package com.woowahan.moduchan.service;

import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.domain.user.User;
import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.exception.EmailAlreadyExistsException;
import com.woowahan.moduchan.repository.NormalUserRepository;
import com.woowahan.moduchan.support.AcceptanceTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserServiceTest{

    @Mock
    private NormalUserRepository normalUserRepository;

    @InjectMocks
    private UserService userService;

    @Spy
    private PasswordEncoder passwordEncoder = new MockPasswordEncoder();

    @Test(expected = EmailAlreadyExistsException.class)
    public void createNormalUser_실패_이미_존재하는_이메일() {
        UserDTO userDTO = new UserDTO(1L, "test@naver.com", "qwe123!!",
                "테스트", "010-1234-1234", "올림픽로 295");
        when(normalUserRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);
        userService.createNormalUser(userDTO);
    }


    private class MockPasswordEncoder implements PasswordEncoder {

        @Override
        public String encode(CharSequence rawPassword) {
            return rawPassword.toString();
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return rawPassword.toString().equals(encodedPassword);
        }
    }
}