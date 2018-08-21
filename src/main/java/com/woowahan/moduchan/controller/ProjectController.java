package com.woowahan.moduchan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    @GetMapping("/{pid}")
    public String getProject(@PathVariable("pid") Long pid) {
        return "project-detail";
    }

    @GetMapping("/start")
    public String create() {
        return "create_project";
    }
}
