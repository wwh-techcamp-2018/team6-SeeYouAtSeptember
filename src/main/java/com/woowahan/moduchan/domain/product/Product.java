package com.woowahan.moduchan.domain.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.woowahan.moduchan.domain.project.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long price;
    private Long supplyQuantity;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Project project;

    @Lob
    private String description;
}
