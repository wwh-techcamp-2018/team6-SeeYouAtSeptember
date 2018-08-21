package com.woowahan.moduchan.domain.category;

import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.dto.category.CategoryDTO;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String categoryImageUrl;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "category")
    private List<Project> projects;

    public CategoryDTO toDTO() {
        return new CategoryDTO(id, title, categoryImageUrl);
    }
}
