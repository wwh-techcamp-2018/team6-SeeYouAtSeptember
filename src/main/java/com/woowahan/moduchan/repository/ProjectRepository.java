package com.woowahan.moduchan.repository;

import com.woowahan.moduchan.domain.category.Category;
import com.woowahan.moduchan.domain.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    public List<Project> findTop9ByOrderByIdDesc();

    public List<Project> findTop9ByCategoryOrderByIdDesc(Category category);

    public List<Project> findTop9ByIdLessThanOrderByIdDesc(Long id);

    public List<Project> findTop9ByCategoryAndIdLessThanOrderByIdDesc(Category category, Long id);
}