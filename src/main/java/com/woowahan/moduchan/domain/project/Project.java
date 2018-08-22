package com.woowahan.moduchan.domain.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.woowahan.moduchan.domain.category.Category;
import com.woowahan.moduchan.domain.product.Product;
import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.dto.product.ProductDTO;
import com.woowahan.moduchan.dto.project.ProjectDTO;
import com.woowahan.moduchan.dto.user.UserDTO;
import com.woowahan.moduchan.support.BaseTimeEntity;
import com.woowahan.moduchan.support.S3Util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Entity
@Where(clause = "deleted=false")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String title;
    @Lob
    private String description;
    @NotNull
    private String thumbnailUrl;
    @NotNull
    private Long goalFundRaising;
    @NotNull
    private Date endAt;
    private STATUS status;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Category category;

    @ManyToOne
    @JoinColumn
    private NormalUser owner;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "project")
    private List<Product> products;

    @NotNull
    private boolean deleted;

    public static Project from(ProjectDTO projectDTO, Category category, NormalUser owner) {
        return new ProjectBuilder()
                .title(projectDTO.getTitle())
                .description(projectDTO.getDescription())
                .thumbnailUrl(projectDTO.getThumbnailUrl())
                .goalFundRaising(projectDTO.getGoalFundRaising())
                .endAt(new Date(projectDTO.getEndAt()))
                .status(STATUS.DRAFT)
                .deleted(false)
                .owner(owner)
                .category(category)
                .products(new ArrayList<>())
                .build();
    }

    public Project addProduct(ProductDTO productDTO) {
        products.add(Product.from(productDTO, this));
        return this;
    }

    public Project updateProject(ProjectDTO projectDTO, Category category) {
        this.description = projectDTO.getDescription();
        this.endAt = new Date(projectDTO.getEndAt());
        this.goalFundRaising = projectDTO.getGoalFundRaising();
        this.thumbnailUrl = projectDTO.getThumbnailUrl();
        this.title = projectDTO.getTitle();
        this.category = category;
        //updateProducts(projectDTO.getProducts());
        return this;
    }

    public void delete() {
        deleted = true;
        products.forEach(product -> product.delete());
    }

    public boolean isOwner(UserDTO userDTO) {
        return id == userDTO.getUid();
    }

    public ProjectDTO toDTO() {
        return new ProjectDTO(
                category.toDTO().getId(),
                id,
                title,
                description,
                thumbnailUrl,
                getCreatedAt().getTime(),
                endAt.getTime(),
                status,
                owner.toDTO().getName(),
                products.stream().map(product -> product.toDTO()).collect(Collectors.toList()),
                goalFundRaising
        );
    }

    public String getFileName() {
        if (thumbnailUrl == null)
            return null;
        return S3Util.DIR_NAME + thumbnailUrl.substring(thumbnailUrl.lastIndexOf(S3Util.SLASH));
    }

    private void updateProducts(List<ProductDTO> productDTOs) {
        products.stream().filter(product -> product.isDeleted())
                .forEach(product -> {
                    if (productDTOs.size() == 0)
                        product.delete();

                    if (product.update(productDTOs.get(0))) {
                        productDTOs.remove(0);
                    }
                });
        productDTOs.forEach(productDTO -> products.add(Product.from(productDTO, this)));
    }

    public enum STATUS {
        DRAFT,
        EVALUATING,
        PUBLISHED,
        REJECTED,
        COMPLETE,
        INCOMPLETE
    }
}
