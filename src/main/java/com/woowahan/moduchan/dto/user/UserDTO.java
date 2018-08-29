package com.woowahan.moduchan.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private final static String EMAIL_REGEX = "^[_0-9a-zA-Z-]+@[0-9a-zA-Z]+(.[_0-9a-zA-Z-]+)*$";
    private final static String PASSWORD_REGEX = "^(?=.*?[a-zA-Z])(?=.*?[0-9]).{8,12}$";
    private final static String PHONE_NO_REGEX = "^01[0|1|6-9]-[0-9]{3,4}-[0-9]{4}$";

    @ApiModelProperty(hidden = true)
    @Null(groups = {LoginValid.class, JoinValid.class})
    private Long uid;

    @ApiModelProperty(example = "example@gmail.com", position = 1)
    @NotEmpty(groups = {LoginValid.class, JoinValid.class})
    @Pattern(regexp = EMAIL_REGEX, message = "email형식이 올바르지 않습니다.", groups = JoinValid.class)
    private String email;

    @ApiModelProperty(example = "password", position = 2)
    @NotEmpty(groups = {LoginValid.class, JoinValid.class})
    @Pattern(regexp = PASSWORD_REGEX, message = "password형식이 올바르지 않습니다.", groups = JoinValid.class)
    private String password;

    @ApiModelProperty(example = "name", position = 3)
    @Size(min = 1, max = 20, message = "20자 이하의 이름을 입력해 주세요.", groups = JoinValid.class)
    private String name;

    @ApiModelProperty(example = "010-0000-0000", position = 4)
    @NotEmpty(groups = JoinValid.class)
    @Pattern(regexp = PHONE_NO_REGEX, message = "전화번호형식이 올바르지 않습니다.", groups = JoinValid.class)
    private String phoneNo;

    @NotEmpty(groups = JoinValid.class)
    @ApiModelProperty(example = "address", position = 5)
    private String address;

    public UserDTO erasePassword() {
        this.password = null;
        return this;
    }

    public void update(UserDTO userDTO) {
        this.email = userDTO.getEmail();
        this.name = userDTO.getName();
        this.phoneNo = userDTO.getPhoneNo();
        this.address = userDTO.getAddress();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // @Validated를 위한 인터페이스
    public interface LoginValid {
    }

    public interface JoinValid {
    }
}
