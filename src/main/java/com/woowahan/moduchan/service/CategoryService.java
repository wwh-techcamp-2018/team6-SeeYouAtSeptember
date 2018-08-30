package com.woowahan.moduchan.service;

import com.woowahan.moduchan.dto.category.CategoryDTO;
import com.woowahan.moduchan.dto.project.ProjectDTO;
import com.woowahan.moduchan.exception.CategoryNotFoundException;
import com.woowahan.moduchan.repository.CategoryRepository;
import com.woowahan.moduchan.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    public static final int PROJECTS_PER_PAGE = 9;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Cacheable(value = "categories")
    public List<CategoryDTO> getCategories() {
        return categoryRepository.findAll().stream().map(category -> category.toDTO()).collect(Collectors.toList());
    }

    public CategoryDTO getCategory(Long cid) {
        return categoryRepository.findById(cid)
                .orElseThrow(() -> new CategoryNotFoundException())
                .toDTO();
    }

    public List<ProjectDTO> getProjectsOfAllCategory(Long lastIndex) {
        if (lastIndex == 0) {
            return projectRepository.findTop9ByOrderByIdDesc()
                    .stream().map(project -> project.toDTO()).collect(Collectors.toList());
        }
        return projectRepository.findTop9ByIdLessThanOrderByIdDesc(lastIndex)
                .stream().map(project -> project.toDTO()).collect(Collectors.toList());
    }

    public List<ProjectDTO> getProjectsOfOneCategory(Long cid, Long lastIndex) {
        if (lastIndex == 0) {
            return projectRepository.findTop9ByCategoryOrderByIdDesc(categoryRepository.findById(cid).orElseThrow(CategoryNotFoundException::new))
                    .stream().map(project -> project.toDTO()).collect(Collectors.toList());
        }
        return projectRepository
                .findTop9ByCategoryAndIdLessThanOrderByIdDesc(categoryRepository.findById(cid).orElseThrow(CategoryNotFoundException::new), lastIndex)
                .stream().map(project -> project.toDTO()).collect(Collectors.toList());
    }
}
