package com.woowahan.moduchan.controller;

import com.woowahan.moduchan.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/start")
    public String create(Model model) {
        model.addAttribute("categories", categoryService.getCategories().stream().
                filter(categoryDTO -> categoryDTO.getId() != 0).collect(Collectors.toList()));
        return "create_project";
    }
}
