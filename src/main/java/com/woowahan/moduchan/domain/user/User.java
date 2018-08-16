package com.woowahan.moduchan.domain.user;

import lombok.Getter;

import javax.persistence.*;

@MappedSuperclass
@Getter
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;

    @Column(unique = true)
    private String email;
    private String name;
}