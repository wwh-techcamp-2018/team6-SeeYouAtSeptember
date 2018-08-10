package com.woowahan.moduchan.domain.product;

import com.woowahan.moduchan.domain.project.Project;

import javax.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long price;
    private Long supplyQuantity;

    @ManyToOne
    @JoinColumn
    private Project project;

    @Lob
    private String description;
}
