package com.woowahan.moduchan.domain.user;

import com.woowahan.moduchan.domain.project.Project;
import lombok.Getter;

import javax.persistence.Entity;

@Entity
@Getter
public class NormalUser extends User {
    private String phoneNo;
    private String address;

    @Builder
    public NormalUser(String password, String email, String name, String phoneNo, String address) {
        super(password, email, name);
        this.phoneNo = phoneNo;
        this.address = address;
    }

    public static NormalUser from(UserDTO userDTO) {
        NormalUserBuilder normalUserBuilder = new NormalUserBuilder();
        return normalUserBuilder
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .phoneNo(userDTO.getPhoneNo())
                .address(userDTO.getAddress())
                .build();
    }

    public UserDTO toDTO() {
        return new UserDTO(id, password, email, name, phoneNo, address);
    }

    public NormalUser encryptPassword(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
        return this;
    }

    public NormalUser update(UserDTO userDTO) {
        email = userDTO.getEmail();
        name = userDTO.getName();
        phoneNo = userDTO.getPhoneNo();
        address = userDTO.getAddress();
        return this;
    }

    public boolean matchPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.password);
    }
}
