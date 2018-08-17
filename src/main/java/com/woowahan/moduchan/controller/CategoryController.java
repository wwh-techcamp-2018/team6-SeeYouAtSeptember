package com.woowahan.moduchan.controller;

import com.woowahan.moduchan.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @GetMapping("")
    public String collectProjects() {
        return "/category";
    }
}
