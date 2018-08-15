package com.woowahan.moduchan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
    @GetMapping("/join")
    public String join() {
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}
