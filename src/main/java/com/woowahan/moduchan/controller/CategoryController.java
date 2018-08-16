package com.woowahan.moduchan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @GetMapping("/{cid}")
    public String showCategoryHTML(@PathVariable Long cid) {
        return "category";
    }
}
