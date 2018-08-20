package com.woowahan.moduchan.service;

import com.woowahan.moduchan.domain.category.Category;
import com.woowahan.moduchan.dto.category.CategoryDTO;
import com.woowahan.moduchan.dto.project.ProjectGatherDTO;
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

    public List<ProjectGatherDTO> getProjects(Long cid, Long lastIndex) {
        if (cid == 0) {
            return getProjectsOfAllCategory(lastIndex);
        }
        return getProjectsOfOneCategory(cid, lastIndex);
    }

    private List<ProjectGatherDTO> getProjectsOfAllCategory(Long lastIndex) {
        if (lastIndex == 0) {
            return projectRepository.findTop9ByOrderByIdDesc()
                    .stream().map(project -> project.toDTO()).collect(Collectors.toList());
        }
        return projectRepository.findTop9ByIdLessThanOrderByIdDesc(lastIndex)
                .stream().map(project -> project.toDTO()).collect(Collectors.toList());
    }

    private List<ProjectGatherDTO> getProjectsOfOneCategory(Long cid, Long lastIndex) {
        if (lastIndex == 0) {
            return projectRepository.findTop9ByCategoryOrderByIdDesc(categoryRepository.findById(cid).orElseThrow(RuntimeException::new))
                    .stream().map(project -> project.toDTO()).collect(Collectors.toList());
        }
        return projectRepository
                .findTop9ByCategoryAndIdLessThanOrderByIdDesc(categoryRepository.findById(cid).orElseThrow(RuntimeException::new), lastIndex)
                .stream().map(project -> project.toDTO()).collect(Collectors.toList());
    }
}
