package com.woowahan.moduchan.domain.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.woowahan.moduchan.domain.category.Category;
import com.woowahan.moduchan.domain.user.NormalUser;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Project {
    public enum STATUS {
        DRAFT,
        EVALUATING,
        PUBLISHED,
        REJECTED,
        COMPLETE,
        INCOMPLETE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 프로젝트 정보 */
    private String title;
    @Lob
    private String description;
    private String thumbnailUrl;

    /* 모금 */
    private Long currentFundRaising;
    private Long goalFundRaising;
    private Long price;
    private Long quantity;

    /* 시간 */
    private Date startAt;
    private Date endAt;

    /* 사람 */
    @ManyToOne
    @JoinColumn
    private NormalUser owner;

    @ManyToMany
    @JsonIgnore
    private List<NormalUser> supporters;

    private STATUS status;
    @ManyToOne
    @JoinColumn
    private Category category;
}
