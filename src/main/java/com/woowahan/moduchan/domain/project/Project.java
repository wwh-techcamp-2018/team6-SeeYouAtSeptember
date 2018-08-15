package com.woowahan.moduchan.domain.project;

import com.woowahan.moduchan.domain.category.Category;
import com.woowahan.moduchan.domain.product.Product;
import com.woowahan.moduchan.domain.user.NormalUser;
import com.woowahan.moduchan.support.BaseTimeEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Project extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /* 프로젝트 정보 */
    private String title;
    @Lob
    private String description;
    private String thumbnailUrl;
    /* 모금 */
    private Long goalFundRaising;
    /* 시간 */
    private Date startAt;
    private Date endAt;
    /* 사람 */
    @ManyToOne
    @JoinColumn
    private NormalUser owner;
    private STATUS status;
    @ManyToOne
    @JoinColumn
    private Category category;
    @OneToMany(mappedBy = "project")
    private List<Product> products;

    public enum STATUS {
        DRAFT,
        EVALUATING,
        PUBLISHED,
        REJECTED,
        COMPLETE,
        INCOMPLETE
    }
}
