package me.leegiseok.project;

import me.leegiseok.project.domain.Article;
import me.leegiseok.project.dto.AddArticleRequest;
import me.leegiseok.project.dto.UpdateArticleRequest;
import me.leegiseok.project.repository.ArticleRepository;
import me.leegiseok.project.service.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ArticleTest {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    @DisplayName("글 작성 테스트")
     void create() {
        AddArticleRequest request = new AddArticleRequest("제목", "내용");
        String author = "Leegiseok";

        Article saved = articleService.save(request ,author);

        Article found = articleRepository.findById(saved.getId())
                .orElseThrow(() -> new IllegalArgumentException("글이 저장되지 않음"));

        assertThat(found.getTitle()).isEqualTo("제목");
        assertThat(found.getContent()).isEqualTo("내용");
        assertThat(found.getAuthor()).isEqualTo("Leegiseok");


    }
    @Test
    @DisplayName("본인 글 삭제  ")
    void  delete() {
        AddArticleRequest request = new AddArticleRequest("제목", "내용");
        String author  ="giseok";
        Article saved = articleService.save(request, author);
        articleService.delete(saved.getId(), "giseok");

        Article found =  articleRepository.findById(saved.getId()).orElseThrow();

        assertThat(found.isDeleted()).isTrue();
        assertThat(found.getDeletedBy()).isEqualTo("giseok");



    }
    @Test
    @DisplayName("다른 사람 글 삭제 ")
    void otherdelete() {
        AddArticleRequest request = new AddArticleRequest("제목" ,"내용");
        String author = "giseok";
        Article saved = articleService.save(request,author);

        assertThrows(SecurityException.class,() -> articleService.delete(saved.getId(), "other"));

        Article found  = articleRepository.findById(saved.getId()).orElseThrow();

        assertThat(found.isDeleted()).isFalse();



    }

    @Test
    @DisplayName("글 수정")
    void put() {
        AddArticleRequest request = new AddArticleRequest("제목", "내용");
        String author = "giseok";
        Article saved= articleService.save(request,author);
        UpdateArticleRequest updaterequest = new UpdateArticleRequest("새 제목", "새 내용");

        Article found =  articleService.update(saved.getId(), updaterequest,  "giseok");

        assertThat(found.getId()).isEqualTo(saved.getId());
        assertThat(found.getTitle()).isEqualTo("새 제목");
        assertThat(found.getContent()).isEqualTo("새 내용");
    }

    @Test
    @DisplayName("조회 실패")
    void foundfail() {
        assertThrows(IllegalArgumentException.class,
                () -> articleService.findById(99L));

    }
    @Test
    @DisplayName("수정 실패")
    void updatefail() {
        AddArticleRequest request = new AddArticleRequest("제목", "내용");
        String author = "giseok";
        Article saved= articleService.save(request,author);
        UpdateArticleRequest updaterequest = new UpdateArticleRequest("새 제목","새 내용");

        assertThrows(SecurityException.class,
                () -> articleService.update(saved.getId(), updaterequest,"other"));
        Article fail = articleRepository.findById(saved.getId()).orElseThrow();
        assertThat(fail.getTitle()).isEqualTo("제목");
        assertThat(fail.getContent()).isEqualTo("내용");

    }






}
