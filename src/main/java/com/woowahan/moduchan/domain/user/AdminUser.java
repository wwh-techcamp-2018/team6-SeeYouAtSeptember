package com.woowahan.moduchan.domain.user;

import javax.persistence.Entity;

@Entity
public class AdminUser extends User {
    public AdminUser(String password, String email, String name) {
        super(password, email, name);
    }
}
