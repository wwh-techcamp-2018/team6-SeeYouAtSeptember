package com.woowahan.moduchan.controller;

import java.util.List;

import com.woowahan.moduchan.domain.product.Product;
import com.woowahan.moduchan.domain.project.Project;
import com.woowahan.moduchan.dto.project.ProjectDTO;
import com.woowahan.moduchan.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/projects")
public class ApiProjectController {
//    전체 프로젝트 조회: GET /api/projects
//    전체 프로젝트 조회: GET /api/projects/page/{pageNo}
//    프로젝트 갱신: PUT /api/projects
//    프로젝트 삭제: DELETE /api/projects/{pid}
//    프로젝트 생성: POST /api/projects —> 상세 페이지 url
    @Autowired
    private ProjectService projectService;

    @GetMapping("")
    public ResponseEntity<List<Project>> getProjects(){
        return new ResponseEntity<>(projectService.getProjects(),HttpStatus.OK);
    }

    @GetMapping("/page/{pageNo}")
    public ResponseEntity<List<Project>> getProjectsPage(@PathVariable int pageNo){
        return new ResponseEntity<>(projectService.getProjectPage(pageNo),HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Void> insertProject(HttpSession session, @RequestBody ProjectDTO projectDTO){
        //TODO 로그인 유저 판별.
        projectService.insertProject(projectDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
