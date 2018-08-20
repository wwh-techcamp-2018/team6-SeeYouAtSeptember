package com.woowahan.moduchan.service;

import com.woowahan.moduchan.dto.category.CategoryDTO;
import com.woowahan.moduchan.dto.project.ProjectDTO;
import com.woowahan.moduchan.repository.CategoryRepository;
import com.woowahan.moduchan.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public CategoryDTO getCategory(Long id) {
        // TODO: 2018. 8. 20. Need custom error: CategoryNotFoundException
        return categoryRepository.findById(id).orElseThrow(RuntimeException::new).toDTO();
    }

    @Cacheable(value = "projects", condition = "#pageNo<2", key = "#id.toString()+#pageNo.toString()")
    public List<ProjectDTO> getCategoryPage(Long id, int pageNo) {
        if (id == 0) {
            return getTotalCategoryPage(pageNo);
        }
        return projectRepository.findByCategory(categoryRepository.findById(id).orElse(null),
                PageRequest.of(pageNo, PROJECTS_PER_PAGE, new Sort(Sort.Direction.DESC, "createdAt")))
                .getContent()
                .stream().map(project -> project.toDTO()).collect(Collectors.toList());
    }

    private List<ProjectDTO> getTotalCategoryPage(int pageNo) {
        return projectRepository.findAll(PageRequest.of(pageNo, CategoryService.PROJECTS_PER_PAGE,
                new Sort(Sort.Direction.DESC, "createdAt"))).getContent()
                .stream().map(project -> project.toDTO()).collect(Collectors.toList());
    }
}
