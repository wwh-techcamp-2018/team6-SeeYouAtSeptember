package com.woowahan.moduchan.service;

import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.exception.EmailAlreadyExistsException;
import com.woowahan.moduchan.repository.NormalUserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Mock
    NormalUserRepository normalUserRepository;

    @InjectMocks
    UserService userService;

    @Test(expected = EmailAlreadyExistsException.class)
    public void createNormalUser_실패_이미_존재하는_이메일() {
        UserDTO userDTO = new UserDTO(1L, "test@naver.com", "qwe123!!",
                "테스트", "010-1234-1234", "올림픽로 295");
        when(normalUserRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);
        userService.createNormalUser(userDTO);
    }
}