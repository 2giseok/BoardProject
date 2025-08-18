package me.leegiseok.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.leegiseok.project.domain.Article;
import me.leegiseok.project.dto.AddArticleRequest;
import me.leegiseok.project.dto.ArticleResponse;
import me.leegiseok.project.dto.PageResponse;
import me.leegiseok.project.dto.UpdateArticleRequest;
import me.leegiseok.project.service.ArticleService;
import org.apache.coyote.Response;
import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/articles")
public class ArticlesApiController {
    private  final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleResponse> create (
            @Valid @RequestBody AddArticleRequest request,
            Principal principal) {
        Article saved = articleService.save(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ArticleResponse(saved));


    }
    @GetMapping("/{id}")
    public  ResponseEntity<ArticleResponse> get(@PathVariable Long id) {
        var article = articleService.findById(id);
        return ResponseEntity.ok(new ArticleResponse(article));
    }
    @GetMapping
    public  ResponseEntity<PageResponse<ArticleResponse>> List (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var result = articleService.findPage(PageRequest.of(page, size , Sort.by("id").descending()));
        return ResponseEntity.ok(PageResponse.of( result.map(ArticleResponse::new)));


    }
    @PatchMapping("/{id}")
    public  ResponseEntity<ArticleResponse> update (
            @PathVariable Long id,
            @Valid @RequestBody UpdateArticleRequest request,
            Principal principal

    ) {
        var updated = articleService.update(id, request, principal.getName());
        return  ResponseEntity.ok(new ArticleResponse(updated));
    }
    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> delete(@PathVariable Long id ,Principal principal) {
        articleService.delete(id, principal.getName());
        return  ResponseEntity.noContent().build();
    }


}
