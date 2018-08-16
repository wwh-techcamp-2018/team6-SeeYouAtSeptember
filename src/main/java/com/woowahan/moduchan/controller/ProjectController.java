package com.woowahan.moduchan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/project")
public class ProjectController {
//    전체 프로젝트 조회: GET /api/projects
//    전체 프로젝트 조회: GET /api/projects/page/{pageNo}
//    프로젝트 갱신: PUT /api/projects
//    프로젝트 삭제: DELETE /api/projects/{pid}
//    프로젝트 생성: POST /api/projects —> 상세 페이지 url
//    프로젝트 생성 페이지: GET /projects/new —> 프로젝트 생성.html
//    상세 페이지 조회: GET /projects/{pid}
//    프로젝트 갱신: GET /projects/{pid}/update —> 업데이트.html


    @GetMapping("")
    public String list() {
        return "";
    }
}
