package com.woowahan.moduchan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    @ApiModelProperty(hidden = true)
    @Null(groups = {LoginValid.class, JoinValid.class})
    private Long id;
    @ApiModelProperty(example = "password", position = 2)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(groups = {LoginValid.class, JoinValid.class})
    private String password;
    @ApiModelProperty(example = "example@gmail.com", position = 1)
    @NotEmpty(groups = {LoginValid.class, JoinValid.class})
    private String email;
    @ApiModelProperty(example = "name", position = 3)
    @NotEmpty(groups = JoinValid.class)
    private String name;
    @ApiModelProperty(example = "010-0000-0000", position = 4)
    @NotEmpty(groups = JoinValid.class)
    private String phoneNo;
    @NotEmpty(groups = JoinValid.class)
    @ApiModelProperty(example = "address", position = 5)
    private String address;

    public UserDTO erasePassword() {
        this.password = null;
        return this;
    }

    public UserDTO eraseEmail() {
        this.email = null;
        return this;
    }

    public UserDTO eraseName() {
        this.name = null;
        return this;
    }

    public UserDTO erasePhoneNo() {
        this.phoneNo = null;
        return this;
    }

    public UserDTO eraseAddress() {
        this.address = null;
        return this;
    }

    public void update(UserDTO userDTO) {
        this.name = userDTO.getName();
        this.phoneNo = userDTO.getPhoneNo();
        this.address = userDTO.getAddress();
    }

    //@Validated를 위한 인터페이스
    public interface LoginValid {
    }

    public interface JoinValid {
    }
}
