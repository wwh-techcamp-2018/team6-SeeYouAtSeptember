package com.woowahan.moduchan.domain.user;

import com.woowahan.moduchan.dto.user.UserDTO;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;

@Entity
@Where(clause = "deleted=false")
@NoArgsConstructor
public class NormalUser extends User {
    private String phoneNo;
    private String address;

    @Builder
    private NormalUser(String password, String email, String name, String phoneNo, String address) {
        super(password, email, name);
        this.phoneNo = phoneNo;
        this.address = address;
    }

    public static NormalUser from(UserDTO userDTO) {
        NormalUserBuilder normalUserBuilder = new NormalUserBuilder();
        return normalUserBuilder
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .name(userDTO.getName())
                .phoneNo(userDTO.getPhoneNo())
                .address(userDTO.getAddress())
                .build();
    }

    public NormalUser encryptPassword(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
        return this;
    }

    public boolean matchPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.password);
    }

    public NormalUser update(UserDTO userDTO) {
        email = userDTO.getEmail();
        name = userDTO.getName();
        phoneNo = userDTO.getPhoneNo();
        address = userDTO.getAddress();
        return this;
    }

    public UserDTO toDTO() {
        return new UserDTO(id, email, password, name, phoneNo, address);
    }
}
