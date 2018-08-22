package com.woowahan.moduchan.domain.product;

import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.dto.product.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Where(clause = "deleted=false")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Long price;
    private Long quantitySupplied;

    @Lob
    private String description;

    @ManyToOne
    @JoinColumn
    private Project project;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "product")
    private List<ProductUserMap> productUserMaps;

    @NotNull
    private boolean deleted = false;

    public static Product from(ProductDTO productDTO, Project project) {
        return new ProductBuilder()
                .title(productDTO.getTitle())
                .price(productDTO.getPrice())
                .quantitySupplied(productDTO.getQuantitySupplied())
                .description(productDTO.getDescription())
                .project(project)
                .deleted(false)
                .build();
    }

    public void delete() {
        this.deleted = true;
    }

    public ProductDTO toDTO() {
        return new ProductDTO(id, title, price, quantitySupplied, description, getSupporterCount(), getQuantityConsumed());
    }

    private int getSupporterCount() {
        return productUserMaps.size();
    }

    private Long getQuantityConsumed() {
        return productUserMaps.stream()
                .map(productUserMap -> productUserMap.getQuantity())
                .reduce(0L, (x, y) -> x + y);
    }
}
