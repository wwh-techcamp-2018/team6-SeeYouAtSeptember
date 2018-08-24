package com.woowahan.moduchan.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    @ApiModelProperty(hidden = true)
    @Null(groups = {LoginValid.class, JoinValid.class})
    private Long uid;

    @ApiModelProperty(example = "example@gmail.com", position = 1)
    @NotEmpty(groups = {LoginValid.class, JoinValid.class})
    private String email;

    @ApiModelProperty(example = "password", position = 2)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(groups = {LoginValid.class, JoinValid.class})
    private String password;

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

    public void update(UserDTO userDTO) {
        this.email = userDTO.getEmail();
        this.name = userDTO.getName();
        this.phoneNo = userDTO.getPhoneNo();
        this.address = userDTO.getAddress();
    }

    // @Validated를 위한 인터페이스
    public interface LoginValid {
    }

    public interface JoinValid {
    }
}
