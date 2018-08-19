package com.woowahan.moduchan.domain.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.woowahan.moduchan.domain.project.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @Column(columnDefinition = "bool default false")
    private boolean deleted = false;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<ProductUserMap> productUserMapList;

    public Product addProject(Project project) {
        this.project = project;
        return this;
    }

    public Product erasePid() {
        this.id = null;
        return this;
    }

    public void delete() {
        this.deleted = true;
    }

    public Long addFunds(Long currentFunds) {
        Long quantity = 0L;
        for(ProductUserMap productUserMap : productUserMapList) {
            quantity = productUserMap.addQuantityToTotal(quantity);
        }
        return currentFunds + quantity * price;
    }
}
