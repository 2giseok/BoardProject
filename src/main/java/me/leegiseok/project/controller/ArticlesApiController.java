package me.leegiseok.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import me.leegiseok.project.domain.Article;
import me.leegiseok.project.dto.AddArticleRequest;
import me.leegiseok.project.dto.ArticleResponse;
import me.leegiseok.project.dto.PageResponse;
import me.leegiseok.project.dto.UpdateArticleRequest;
import me.leegiseok.project.service.ArticleService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/articles")
@Validated
@Tag(name = "Article API", description = "게시글 CRUD등록 , 페이지 ")
public class ArticlesApiController {
    private  final ArticleService articleService;

    @Operation(summary =  "글 작성", description = "사용자가 글을 작성")

    @PostMapping
    public ResponseEntity<ArticleResponse> create (
            @Valid @RequestBody AddArticleRequest request,
            Principal principal) {
        Article saved = articleService.save(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ArticleResponse(saved));


    }
    @Operation(summary = "특정 게시물 조회 ", description = "ID로 글 조회 ")
    @GetMapping("/{id}")
    public  ResponseEntity<ArticleResponse> get(@PathVariable Long id) {
        var article = articleService.findById(id);
        return ResponseEntity.ok(new ArticleResponse(article));
    }
    @Operation(summary =  "글 목록", description = "페이지 정렬(추가 예정 기본은 최신순),검색 기능 ( 추가 예정 )  지원 " )
    @Parameters( {
            @Parameter(name = "page", description =  "0부터 시작 "),
            @Parameter(name = "size", description =  " 최대  50 "),
            @Parameter(name = "sort", description =  "정렬")

    })
    @GetMapping
    public  ResponseEntity<PageResponse<ArticleResponse>> List (
          @PositiveOrZero @RequestParam(defaultValue = "0") int page,
          @Min(1) @Max(50) @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "id,desc") String sort,
          @RequestParam(required = false) String q
    ) {
        if( sort ==null || sort.isBlank()) sort = "id,desc";
        String[] orders = sort.split(",");
        String sortProp = orders[0].trim();
        String dirStr = (orders.length > 1 ? orders[1].trim(): "desc");
        Sort.Direction direction ="asc".equalsIgnoreCase(dirStr)
                ? Sort.Direction.ASC: Sort.Direction.DESC;

        var pageable = PageRequest.of(page,size,Sort.by(direction, sortProp));

        var p= articleService.findPage(pageable).map(ArticleResponse::new);
                var body = PageResponse.of(p);


       return ResponseEntity.ok().header("X-Total-Count",String.valueOf(body.totalElements()))
               .body(body);


    }
    @Operation(summary = "글 수정", description = "작성자 본인만 수정 가능")
    @PatchMapping("/{id}")
    public  ResponseEntity<ArticleResponse> update (
            @PathVariable Long id,
            @Valid @RequestBody UpdateArticleRequest request,
            Principal principal

    ) {
        var updated = articleService.update(id, request, principal.getName());
        return  ResponseEntity.ok(new ArticleResponse(updated));
    }
    @Operation(summary = "글 삭제", description = "작성자 본인만 삭제 가능")
    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> delete(@PathVariable Long id ,Principal principal) {
        articleService.delete(id, principal.getName());
        return  ResponseEntity.noContent().build();
    }


}
