package me.leegiseok.project.controller;

import lombok.RequiredArgsConstructor;
import me.leegiseok.project.dto.LoginRequest;
import me.leegiseok.project.dto.LoginResponse;
import me.leegiseok.project.dto.SignupRequest;
import me.leegiseok.project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @RequestMapping("/login")
    public  String loginPage() {


        return "login/login";
    }

   /* @PostMapping("/login")
    public  String login(@ModelAttribute LoginRequest request, Model model) {
        try {
            LoginResponse response = userService.login(request);
            model.addAttribute("nickname", response.getNickname());
            return "redirect:/articles";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "login/login";
        }
    } */

    @GetMapping("/signup")
    public  String signupPage() {
        return "signup/signup";

    }
    @PostMapping("/signup")
    public  String signup(@ModelAttribute SignupRequest request) {
        userService.signup(request);
        return "redirect:/login/login";
    }

}
