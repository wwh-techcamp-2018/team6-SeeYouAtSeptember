package com.woowahan.moduchan.domain.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.woowahan.moduchan.domain.category.Category;
import com.woowahan.moduchan.domain.product.Product;
import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.dto.project.ProjectDTO;
import com.woowahan.moduchan.dto.project.ProjectGatherDTO;
import com.woowahan.moduchan.support.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Entity
@Getter
@NoArgsConstructor
public class Project extends BaseTimeEntity {

    private static final int CURRENT_DATE = (1000*60*60*24);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /* 프로젝트 정보 */
    @NotNull
    private String title;
    @Lob
    private String description;
    @NotNull
    private String thumbnailUrl;
    /* 모금 */
    @NotNull
    private Long goalFundRaising;
    /* 시간 */
    @NotNull
    private Date endAt;
    /*상태*/
    @Column(columnDefinition = "integer default 1")
    private STATUS status;

    @NotNull
    @Column(columnDefinition = "bool default false")
    private boolean deleted = false;

    /* 사람 */
    @ManyToOne
    @JoinColumn
    private NormalUser owner;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Category category;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "project")
    private List<Product> products = new ArrayList<>();

    @Builder
    public Project(String title, String description, String thumbnailUrl, Long goalFundRaising, Date endAt) {
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.goalFundRaising = goalFundRaising;
        this.endAt = endAt;
    }

    public static Project from(ProjectDTO projectDTO) {
        return new ProjectBuilder().title(projectDTO.getTitle())
                .description(projectDTO.getDescription())
                .endAt(projectDTO.getEndAt())
                .goalFundRaising(projectDTO.getGoalFundRaising())
                .thumbnailUrl(projectDTO.getThumbnailUrl())
                .build();
    }

    public Project addCategory(Category category) {
        this.category = category;
        return this;
    }

    public Project addUser(NormalUser user) {
        this.owner = user;
        return this;
    }

    public void addProducts(List<Product> productList) {
        productList.forEach(product -> this.products.add(product.erasePid().addProject(this)));
    }

    public Project updateProject(ProjectDTO projectDTO, Category category) {
        this.description = projectDTO.getDescription();
        this.endAt = projectDTO.getEndAt();
        this.goalFundRaising = projectDTO.getGoalFundRaising();
        this.thumbnailUrl = projectDTO.getThumbnailUrl();
        this.title = projectDTO.getTitle();
        this.category = category;
        return this;
    }

    public void delete() {
        deleted = true;
        products.forEach(product -> product.delete());
    }

    public ProjectGatherDTO toDTO(){
        return new ProjectGatherDTO(id,title,owner.getName(),thumbnailUrl, calculatePeriod() , getCurrentFunds());
    }

    public Long getCurrentFunds() {
        Long currentFunds = 0L;
        for(Product product: products) {
            currentFunds = product.addFunds(currentFunds);
        }
        return currentFunds;
    }

    private int calculatePeriod() {
        if(endAt == null) return 0;
        return (int)(endAt.getTime()-new Date().getTime())/CURRENT_DATE;
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
