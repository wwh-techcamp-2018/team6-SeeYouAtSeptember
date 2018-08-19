package com.woowahan.moduchan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    //@Validated를 위한 인터페이스
    public interface LoginValid{}

    public interface JoinValid{}

    @Null(groups = {LoginValid.class, JoinValid.class})
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(groups = {LoginValid.class, JoinValid.class})
    private String password;

    @NotEmpty(groups = {LoginValid.class, JoinValid.class})
    private String email;

    @NotEmpty(groups = JoinValid.class)
    private String name;

    @NotEmpty(groups = JoinValid.class)
    private String phoneNo;

    @NotEmpty(groups = JoinValid.class)
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
}
