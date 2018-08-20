package com.woowahan.moduchan.domain.product;

import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.dto.product.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Where(clause = "deleted=false")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long price;
    private Long supplyQuantity;

    @Lob
    private String description;

    @ManyToOne
    @JoinColumn
    private Project project;

    @NotNull
    private boolean deleted = false;

    public static Product from(ProductDTO productDTO, Project project) {
        return new ProductBuilder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .supplyQuantity(productDTO.getSupplyQuantity())
                .description(productDTO.getDescription())
                .project(project)
                .deleted(false)
                .build();
    }

    public void delete() {
        this.deleted = true;
    }

    public ProductDTO toDTO() {
        return new ProductDTO(id, name, price, supplyQuantity, description);
    }
}
