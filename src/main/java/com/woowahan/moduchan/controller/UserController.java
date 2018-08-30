package com.woowahan.moduchan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/users")
public class UserController {
    public final static String REDIR_URL = "redirectUrl";

    @GetMapping("/join")
    public String join() {
        return "/join";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        model.addAttribute(REDIR_URL, (request.getHeader("Referer")));
        return "/login";
    }
}
