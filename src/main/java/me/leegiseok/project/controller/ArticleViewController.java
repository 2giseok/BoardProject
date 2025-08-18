package me.leegiseok.project.controller;

import lombok.RequiredArgsConstructor;
import me.leegiseok.project.dto.ArticleResponse;
import me.leegiseok.project.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
