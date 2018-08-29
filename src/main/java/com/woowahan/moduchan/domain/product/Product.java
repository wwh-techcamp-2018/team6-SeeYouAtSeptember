package com.woowahan.moduchan.domain.product;

import com.woowahan.moduchan.domain.order.OrderHistory;
import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.dto.product.ProductDTO;
import com.woowahan.moduchan.exception.NotEnoughQuantityException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Where(clause = "deleted=false")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Long price;
    private Long quantitySupplied;
    private Long quantityRemained;

    @Lob
    private String description;

    @ManyToOne
    @JoinColumn
    private Project project;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "product")
    private List<OrderHistory> orderHistories;

    @NotNull
    private boolean deleted = false;

    public static Product from(ProductDTO productDTO, Project project) {
        return new ProductBuilder()
                .title(productDTO.getTitle())
                .price(productDTO.getPrice())
                .quantitySupplied(productDTO.getQuantitySupplied())
                .quantityRemained(productDTO.getQuantitySupplied())
                .description(productDTO.getDescription())
                .project(project)
                .deleted(false)
                .build();
    }

    public Project getProject() {
        return this.project;
    }

    public void delete() {
        if (orderHistories.stream().filter(orderHistory -> !orderHistory.isSuccess()).count() != 0) {
            // TODO: 2018. 8. 22. 후원자가 존재하는데 삭제한 경우, 에러를 반환한다. 
            throw new RuntimeException();
        }
        this.deleted = true;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public ProductDTO toDTO() {
        return new ProductDTO(id, title, price, quantitySupplied, description, getSupporters(), quantityRemained);
    }

    private Set<NormalUser> getSupporters() {
        return orderHistories.stream().map(orderHistory -> orderHistory.getNormalUser())
                .collect(Collectors.toSet());
    }

    public boolean update(ProductDTO productDTO) {
        if (productDTO.getPid() != id) {
            this.deleted = true;
            return false;
        }

        if (orderHistories.stream().filter(orderHistory -> !orderHistory.isSuccess()).count() == 0) {
            updateAll(productDTO);
        } else {
            updateDescription(productDTO.getDescription());
        }
        return true;
    }

    private void updateAll(ProductDTO productDTO) {
        this.title = productDTO.getTitle();
        this.price = productDTO.getPrice();
        this.quantitySupplied = productDTO.getQuantitySupplied();
        this.quantityRemained = productDTO.getQuantitySupplied();
        this.description = productDTO.getDescription();
    }

    private void updateDescription(String description) {
        this.description = description;
    }

    public Product updateRemainQuantity(Long orderQuantity) {
        if(orderQuantity > quantityRemained){
            throw new NotEnoughQuantityException();
        }
        quantityRemained -= orderQuantity;
        return this;
    }

}
