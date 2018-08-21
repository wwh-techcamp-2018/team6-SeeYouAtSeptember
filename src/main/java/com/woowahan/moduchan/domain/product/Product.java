package com.woowahan.moduchan.domain.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.dto.product.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

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
    private Long supplyQuantity;

    @Lob
    private String description;

    @JsonBackReference
    @OneToMany(mappedBy = "product")
    private List<ProductUserMap> productUserMaps;

    @ManyToOne
    @JoinColumn
    private Project project;

    @NotNull
    private boolean deleted = false;

    public static Product from(ProductDTO productDTO, Project project) {
        return new ProductBuilder()
                .title(productDTO.getTitle())
                .price(productDTO.getPrice())
                .supplyQuantity(productDTO.getSupplyQuantity())
                .description(productDTO.getDescription())
                .project(project)
                .deleted(false)
                .build();
    }

    public void delete() {
        if (productUserMaps.stream().filter(productUserMap -> !productUserMap.isDeleted()).collect(Collectors.toList()).size() != 0) {
            // TODO: 2018. 8. 22. 후원자가 존재하는데 삭제한 경우, 에러를 반환한다. 
            throw new RuntimeException();
        }
        this.deleted = true;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public ProductDTO toDTO() {
        return new ProductDTO(id, title, price, supplyQuantity, description);
    }

    public boolean update(ProductDTO productDTO) {
        if (productDTO.getPid() != id) {
            this.deleted = true;
            return false;
        }

        if (productUserMaps.stream().filter(productUserMap -> !productUserMap.isDeleted()).collect(Collectors.toList()).size() == 0) {
            updateAll(productDTO);
        } else {
            updateDescription(productDTO.getDescription());
        }
        return true;
    }

    private void updateAll(ProductDTO productDTO) {
        this.title = productDTO.getTitle();
        this.price = productDTO.getPrice();
        this.supplyQuantity = productDTO.getSupplyQuantity();
        this.description = productDTO.getDescription();
    }

    private void updateDescription(String description) {
        this.description = description;
    }
}
