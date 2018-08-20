package com.woowahan.moduchan.domain.user;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@MappedSuperclass
@NoArgsConstructor
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(unique = true)
    protected String email;

    protected String password;
    protected String name;

    @NotNull
    protected boolean deleted;

    public User(String password, String email, String name) {
        this.password = password;
        this.email = email;
        this.name = name;
    }

    public void delete() {
        this.deleted = true;
    }
}
