package com.woowahan.moduchan.domain.user;

import com.woowahan.moduchan.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
public class NormalUser extends User {
    private String phoneNo;
    private String address;

    @Builder
    public NormalUser(String password, String email, String name, String phoneNo, String address) {
        super(password, email, name);
        this.phoneNo = phoneNo;
        this.address = address;
    }

    public UserDTO toDTO() {
        return new UserDTO(id, password, email, name, phoneNo, address);
    }

    public static NormalUser from(UserDTO userDTO) {
        NormalUserBuilder normalUserBuilder = new NormalUserBuilder();
        return null;
    }
}
