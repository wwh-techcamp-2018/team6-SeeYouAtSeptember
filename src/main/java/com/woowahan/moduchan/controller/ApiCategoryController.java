package com.woowahan.moduchan.controller;

import com.woowahan.moduchan.dto.category.CategoryDTO;
import com.woowahan.moduchan.dto.project.ProjectDTO;
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
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        return new ResponseEntity<>(categoryService.getCategories(), HttpStatus.OK);
    }

    @ApiOperation(value = "카테고리 조회", notes = "특정 카테고리의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "조회 성공")
            //error에 대한 설명 추가
    })
    @GetMapping("/{cid}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long cid) {
        return new ResponseEntity<>(categoryService.getCategory(cid), HttpStatus.OK);
    }

    @ApiOperation(value = "카테고리별 특정 페이지의 프로젝트 조회", notes = "특정 페이지의 프로젝트 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "조회 성공")
            //error에 대한 설명 추가
    })
    @GetMapping("/{cid}/last/{lastIndex}")
    public ResponseEntity<List<ProjectDTO>> getCategoryPage(@PathVariable("cid") Long cid, @PathVariable("lastIndex") Long lastIndex) {
        return new ResponseEntity<>(categoryService.getCategoryPage(cid, lastIndex), HttpStatus.OK);
    }
}