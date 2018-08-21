package com.woowahan.moduchan.service;

import com.woowahan.moduchan.dto.category.CategoryDTO;
import com.woowahan.moduchan.dto.project.ProjectDTO;
import com.woowahan.moduchan.exception.CategoryNotFoundException;
import com.woowahan.moduchan.repository.CategoryRepository;
import com.woowahan.moduchan.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
                .orElseThrow(() -> new CategoryNotFoundException("cid: " + cid))
                .toDTO();
    }

    public List<ProjectDTO> getCategoryPage(Long cid, Long lastIndex) {
        if (cid == 0) {
            return getProjectsOfAllCategory(lastIndex);
        }
        return getProjectsOfOneCategory(cid, lastIndex);
    }

    private List<ProjectDTO> getProjectsOfAllCategory(Long lastIndex) {
        if (lastIndex == 0) {
            return projectRepository.findTop9ByOrderByIdDesc()
                    .stream().map(project -> project.toDTO()).collect(Collectors.toList());
        }
        return projectRepository.findTop9ByIdLessThanOrderByIdDesc(lastIndex)
                .stream().map(project -> project.toDTO()).collect(Collectors.toList());
    }

    private List<ProjectDTO> getProjectsOfOneCategory(Long cid, Long lastIndex) {
        if (lastIndex == 0) {
            return projectRepository.findTop9ByCategoryOrderByIdDesc(categoryRepository.findById(cid).orElseThrow(RuntimeException::new))
                    .stream().map(project -> project.toDTO()).collect(Collectors.toList());
        }
        return projectRepository
                .findTop9ByCategoryAndIdLessThanOrderByIdDesc(categoryRepository.findById(cid).orElseThrow(RuntimeException::new), lastIndex)
                .stream().map(project -> project.toDTO()).collect(Collectors.toList());
    }
}
