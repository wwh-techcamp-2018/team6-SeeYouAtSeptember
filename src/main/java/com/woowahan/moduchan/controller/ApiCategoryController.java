package com.woowahan.moduchan.controller;

import com.woowahan.moduchan.domain.category.Category;
import com.woowahan.moduchan.dto.category.CategoryDTO;
import com.woowahan.moduchan.dto.project.ProjectGatherDTO;
import com.woowahan.moduchan.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation(value = "카테고리 전체 조회", notes = "모든 카테고리의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "조회 성공")
            //error에 대한 설명 추가
    })
    @GetMapping("")
    public ResponseEntity<List<CategoryDTO>> list() {
        return new ResponseEntity<>(categoryService.getCategories(), HttpStatus.OK);
    }

    @ApiOperation(value = "카테고리 조회", notes = "특정 카테고리의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "조회 성공")
            //error에 대한 설명 추가
    })
    @GetMapping("/{cid}")
    public ResponseEntity<Category> getCategory(@PathVariable("cid") Long cid) {
        return new ResponseEntity<>(categoryService.geCategory(cid), HttpStatus.OK);
    }

    @ApiOperation(value = "카테고리별 특정 페이지의 프로젝트 조회", notes = "특정 페이지의 프로젝트 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "조회 성공")
            //error에 대한 설명 추가
    })
    @GetMapping("/{cid}/page/{pageNo}")
    public ResponseEntity<List<ProjectGatherDTO>> getCategoryPage(@PathVariable("cid") Long cid, @PathVariable("pageNo") int pageNo) {
        return new ResponseEntity<>(categoryService.getCategoryPage(cid, pageNo), HttpStatus.OK);
    }
}
