package com.woowahan.moduchan.controller;

import com.woowahan.moduchan.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("projectsOrderByCurrentFund", projectService.getTop3ByOrderByCurrentFundRaising());
        model.addAttribute("projectsRecommendation", projectService.getRecommendationProjects());
        model.addAttribute("projectsOrderByEndAt", projectService.getTop3ByEndAt());
        return "index";
    }
}
