package com.woowahan.moduchan.domain.user;

import javax.persistence.*;

@MappedSuperclass
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;

    @Column(unique = true)
    private String email;
    private String name;
}