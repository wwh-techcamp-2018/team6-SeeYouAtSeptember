package com.woowahan.moduchan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String email;
    private String name;
    private String phoneNo;
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
}
