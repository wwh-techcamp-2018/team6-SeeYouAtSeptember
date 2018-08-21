package com.woowahan.moduchan.controller;

import com.woowahan.moduchan.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    public final String PROJECT_KEY = "project";

    @Autowired
    ProjectService projectService;

    @GetMapping("/{pid}")
    public String getProject(@PathVariable("pid") Long pid, Model model){
        model.addAttribute(PROJECT_KEY,projectService.getProject(pid));
        return "project-detail";
    }

    @GetMapping("/start")
    public String create() {
        return "create_project";
    }
}
