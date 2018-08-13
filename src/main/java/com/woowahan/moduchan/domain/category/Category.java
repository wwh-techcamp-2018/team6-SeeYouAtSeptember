package com.woowahan.moduchan.domain.category;

import com.woowahan.moduchan.domain.project.Project;

import javax.persistence.*;
import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @OneToMany(mappedBy = "category")
    private List<Project> projects;
}
