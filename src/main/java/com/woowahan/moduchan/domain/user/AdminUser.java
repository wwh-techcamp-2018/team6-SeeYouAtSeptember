package com.woowahan.moduchan.domain.user;

import org.hibernate.annotations.Where;

import javax.persistence.Entity;

@Entity
@Where(clause = "deleted=false")
public class AdminUser extends User {
    public AdminUser(String password, String email, String name) {
        super(password, email, name);
    }
}
