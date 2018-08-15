package com.woowahan.moduchan.service;

import com.woowahan.moduchan.domain.category.Category;
import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.dto.category.CategoryDTO;
import com.woowahan.moduchan.repository.CategoryRepository;
import com.woowahan.moduchan.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CategoryService {
    private static final int PAGE_PROJECT_COUNT = 9;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Cacheable("categories")
    public List<CategoryDTO> getCategories(){
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        categoryRepository.findAll().forEach(category -> categoryDTOList.add(category.toDTO()));
        return categoryDTOList;
    }

    public Category geCategory(Long id){
        //TODO 카테고리를 찾을때 없으면 커스텀 에러 발생
        return categoryRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public List<Project> getCategoryPage(Long id,int pageNo) {
        return projectRepository.findByCategory(categoryRepository.findById(id).orElse(null),
                PageRequest.of(pageNo, PAGE_PROJECT_COUNT, new Sort(Sort.Direction.DESC, "startAt")))
                .getContent();
    }
}
