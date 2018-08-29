package com.woowahan.moduchan.dto.user;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDTOTest {
    private static Validator validator;

    @Before
    public void setUp() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void userValid_성공() {
        UserDTO userDTO = new UserDTO(null, "test@naver.com", "qwe123!!",
                "테스트", "010-1234-1234", "올림픽로 295");
        Set<ConstraintViolation<UserDTO>> constraintViolcations = validator.validate(userDTO, UserDTO.JoinValid.class);
        assertThat(constraintViolcations.size()).isEqualTo(0);
    }

    @Test
    public void userValid_실패_빈_이메일() {
        UserDTO userDTO = new UserDTO(null, "", "qwe123!!",
                "테스트", "010-1234-1234", "올림픽로 295");
        Set<ConstraintViolation<UserDTO>> constraintViolcations = validator.validate(userDTO, UserDTO.JoinValid.class);
        System.out.println(constraintViolcations);
        assertThat(constraintViolcations.size()).isEqualTo(2);
    }

    @Test
    public void userValid_실패_유효하지않은_이메일() {
        UserDTO userDTO = new UserDTO(null, "test", "qwe123!!",
                "테스트", "010-1234-1234", "올림픽로 295");
        Set<ConstraintViolation<UserDTO>> constraintViolcations = validator.validate(userDTO, UserDTO.JoinValid.class);
        assertThat(constraintViolcations.size()).isEqualTo(1);
    }

    @Test
    public void userValid_실패_빈_패스워드() {
        UserDTO userDTO = new UserDTO(null, "test@naver.com", "",
                "테스트", "010-1234-1234", "올림픽로 295");
        Set<ConstraintViolation<UserDTO>> constraintViolcations = validator.validate(userDTO, UserDTO.JoinValid.class);
        assertThat(constraintViolcations.size()).isEqualTo(2);
    }

    @Test
    public void userValid_실패_유효하지않은_패스워드() {
        UserDTO userDTO = new UserDTO(null, "test@naver.com", "qwe",
                "테스트", "010-1234-1234", "올림픽로 295");
        Set<ConstraintViolation<UserDTO>> constraintViolcations = validator.validate(userDTO, UserDTO.JoinValid.class);
        assertThat(constraintViolcations.size()).isEqualTo(1);
    }

    @Test
    public void userValid_실패_빈_이름() {
        UserDTO userDTO = new UserDTO(null, "test@naver.com", "qwe123!!",
                "", "010-1234-1234", "올림픽로 295");
        Set<ConstraintViolation<UserDTO>> constraintViolcations = validator.validate(userDTO, UserDTO.JoinValid.class);
        assertThat(constraintViolcations.size()).isEqualTo(1);
    }

    @Test
    public void userValid_실패_긴_이름() {
        UserDTO userDTO = new UserDTO(null, "test@naver.com", "qwe123!!",
                "테스트12345678901234567890", "010-1234-1234", "올림픽로 295");
        Set<ConstraintViolation<UserDTO>> constraintViolcations = validator.validate(userDTO, UserDTO.JoinValid.class);
        assertThat(constraintViolcations.size()).isEqualTo(1);
    }

    @Test
    public void userValid_빈_전화번호() {
        UserDTO userDTO = new UserDTO(null, "test@naver.com", "qwe123!!",
                "테스트", "", "올림픽로 295");
        Set<ConstraintViolation<UserDTO>> constraintViolcations = validator.validate(userDTO, UserDTO.JoinValid.class);
        assertThat(constraintViolcations.size()).isEqualTo(2);
    }

    @Test
    public void userValid_유효하지않은_전화번호() {
        UserDTO userDTO = new UserDTO(null, "test@naver.com", "qwe123!!",
                "테스트", "01012341234", "올림픽로 295");
        Set<ConstraintViolation<UserDTO>> constraintViolcations = validator.validate(userDTO, UserDTO.JoinValid.class);
        assertThat(constraintViolcations.size()).isEqualTo(1);
    }

    @Test
    public void userValid_빈_주소() {
        UserDTO userDTO = new UserDTO(null, "test@naver.com", "qwe123!!",
                "테스트", "010-1234-1234", "");
        Set<ConstraintViolation<UserDTO>> constraintViolcations = validator.validate(userDTO, UserDTO.JoinValid.class);
        assertThat(constraintViolcations.size()).isEqualTo(1);
    }
}