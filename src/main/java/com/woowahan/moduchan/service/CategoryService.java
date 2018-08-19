package com.woowahan.moduchan.service;

import com.woowahan.moduchan.domain.category.Category;
import com.woowahan.moduchan.dto.category.CategoryDTO;
import com.woowahan.moduchan.dto.project.ProjectGatherDTO;
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
    public static final int PAGE_PROJECT_COUNT = 9;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Cacheable(value = "categories")
    public List<CategoryDTO> getCategories() {
        return categoryRepository.findAll().stream().map(category -> category.toDTO()).collect(Collectors.toList());
    }

    public Category geCategory(Long id) {
        //TODO 카테고리를 찾을때 없으면 커스텀 에러 발생
        return categoryRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Cacheable(value = "projects", condition = "#pageNo<2", key = "#id.toString()+#pageNo.toString()")
    public List<ProjectGatherDTO> getCategoryPage(Long id, int pageNo) {
        if (id == 0) {
            return getTotalCategoryPage(pageNo);
        }
        return projectRepository.findByCategoryAndDeletedFalse(categoryRepository.findById(id).orElse(null),
                PageRequest.of(pageNo, PAGE_PROJECT_COUNT, new Sort(Sort.Direction.DESC, "createdAt")))
                .getContent()
                .stream().map(project -> project.toDTO()).collect(Collectors.toList());
    }

    private List<ProjectGatherDTO> getTotalCategoryPage(int pageNo) {
        return projectRepository.findAllByDeletedFalse(PageRequest.of(pageNo, CategoryService.PAGE_PROJECT_COUNT,
                new Sort(Sort.Direction.DESC, "createdAt"))).getContent()
                .stream().map(project -> project.toDTO()).collect(Collectors.toList());
    }
}
