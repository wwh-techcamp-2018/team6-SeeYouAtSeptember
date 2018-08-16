package com.woowahan.moduchan.domain.user;

import lombok.Getter;

import javax.persistence.*;

@MappedSuperclass
@Getter
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String password;

    @Column(unique = true)
    protected String email;
    protected String name;

    protected boolean deleted = false;

    public User(String password, String email, String name) {
        this.password = password;
        this.email = email;
        this.name = name;
    }

    public void delete() {
        this.deleted = true;
    }
}
