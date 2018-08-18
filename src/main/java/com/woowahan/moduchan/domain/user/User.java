package com.woowahan.moduchan.domain.user;

import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(columnDefinition = "bool default false")
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
