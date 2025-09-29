package me.leegiseok.project.controller;

import lombok.RequiredArgsConstructor;
import me.leegiseok.project.dto.AddArticleRequest;
import me.leegiseok.project.dto.ArticleResponse;
import me.leegiseok.project.repository.UserRepository;
import me.leegiseok.project.service.ArticleService;
import me.leegiseok.project.service.UserService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor

@Controller
@RequestMapping("/articles")

public class ArticleViewController {
    private final ArticleService articleService;


    @GetMapping
    public  String list(Model model) {
        var articles = articleService.findAll().stream()
                .map(ArticleResponse::new).toList();
        model.addAttribute("articles", articles);
        return "articles/list";

    }
    @GetMapping("/{id}")
    public  String detail(@PathVariable Long id, Model model) {
        var article = articleService.findById(id);
        model.addAttribute("article", article);
        return "articles/detail";
    }

    @GetMapping("/new")
    public  String newPost(Model model) {
        return "articles/new";

    }

    /*@PostMapping("/new")
    public  String createPost(@ModelAttribute AddArticleRequest request,  Principal principal) {

        if(principal ==null) return "redirect:/login";

        articleService.save(request, principal.getName());
        return  "redirect:/articles";

    } */
    
}
