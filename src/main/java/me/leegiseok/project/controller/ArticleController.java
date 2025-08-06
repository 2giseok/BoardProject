package me.leegiseok.project.controller;

import lombok.RequiredArgsConstructor;
import me.leegiseok.project.domain.Article;
import me.leegiseok.project.dto.AddArticleRequest;
import me.leegiseok.project.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ArticleController {

    private final ArticleService articleService;
// 작성 페이지
    @GetMapping("/articles/new")
    public  String newArticleform() {
        return "articles/new";

    }

    @PostMapping("/articles/new")
    public  String createArticle(@ModelAttribute AddArticleRequest request,Principal principal) {
        articleService.save(request, principal.getName());
        return "redirect:/articles/list";
    }

//작성 요청 처피
    @PostMapping("/api/articles")
    public ResponseEntity<Article>addArticle(@ModelAttribute AddArticleRequest request, Principal principal) {
        Article savedArticle=  articleService.save(request, principal.getName());
        return ResponseEntity.ok(savedArticle);

    }
// 글 목록
    @GetMapping("/articles/list")
    public  String geArticle(Model model) {
        List<Article> articles = articleService.findAll();
        model.addAttribute("articles", articles);
        return "articles/list";

    }
    //상세 페이지
    @GetMapping("/articles/{id}")
    public  String getArticle(@PathVariable Long id , Model model) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "articles/detail";

    }
    //삭제 페디지
    @DeleteMapping("/api/articles/{id}")
    public  ResponseEntity <Void> deleteArticle(@PathVariable Long id) {
        articleService.delete(id);
        return ResponseEntity.ok().build();

    }
}
