package com.woowahan.moduchan.controller;

import com.woowahan.moduchan.domain.category.Category;
import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.dto.category.CategoryDTO;
import com.woowahan.moduchan.dto.project.ProjectGatherDTO;
import com.woowahan.moduchan.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class ApiCategoryController {
    // TODO: 2018. 8. 14. 스웨거 작성 
    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<CategoryDTO>> list() {
        return new ResponseEntity<>(categoryService.getCategories(), HttpStatus.OK);
    }

    @GetMapping("/{cid}")
    public ResponseEntity<Category> getCategory(@PathVariable("cid") Long cid) {
        return new ResponseEntity<>(categoryService.geCategory(cid), HttpStatus.OK);
    }

    @GetMapping("/{cid}/page/{pageNo}")
    public ResponseEntity<List<ProjectGatherDTO>> getCategoryPage(@PathVariable("cid") Long cid, @PathVariable("pageNo") int pageNo) {
        return new ResponseEntity<>(categoryService.getCategoryPage(cid, pageNo), HttpStatus.OK);
    }
}
