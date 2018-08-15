package com.woowahan.moduchan.controller;

import java.util.List;
import com.woowahan.moduchan.domain.category.Category;
import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.dto.category.CategoryDTO;
import com.woowahan.moduchan.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.Cacheable;

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
    public ResponseEntity<Category> getCategory(@PathVariable("cid") Long cid){
        return new ResponseEntity<>(categoryService.geCategory(cid),HttpStatus.OK);
    }

    @GetMapping("/{cid}/page/{pageNo}")
    public ResponseEntity<List<Project>> getCategoryPage(@PathVariable("cid") Long cid, @PathVariable("pageNo") int pageNo){
        return new ResponseEntity<>(categoryService.getCategoryPage(cid,pageNo),HttpStatus.OK);
    }
}
