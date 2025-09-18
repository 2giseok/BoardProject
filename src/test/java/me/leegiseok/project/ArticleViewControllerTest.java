package me.leegiseok.project;

import me.leegiseok.project.config.jwt.JwtAuthFilter;
import me.leegiseok.project.config.jwt.JwtTokenProvider;
import me.leegiseok.project.controller.ArticleViewController;
import me.leegiseok.project.domain.Article;
import me.leegiseok.project.service.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;



import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ArticleViewController.class,
excludeAutoConfiguration = {ThymeleafAutoConfiguration.class})
public class ArticleViewControllerTest {

    @MockitoBean
    JwtTokenProvider jwtTokenProvider;
    @MockitoBean
    JwtAuthFilter jwtAuthFilter;

@Autowired
    MockMvc mockMvc;
@MockitoBean
    ArticleService articleService;

@Test
    @DisplayName("목록 페이지 검증")
    void list_view()throws  Exception {
    given(articleService.findAll()).willReturn(Collections.singletonList(new Article("title","content","Leegiseok")));

    mockMvc.perform(get("/articles"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("articles"))
            .andExpect(view().name("articles/list"));



}

@Test
    @DisplayName("글 조회")
    void detailArticle() throws Exception {
    Long articleId = 1L;
    Article article = new Article("title", "content", "Leegiseok");
    given(articleService.findById(articleId)).willReturn(article);

    mockMvc.perform(get("/articles/{id}",articleId))
            .andExpect(status().isOk())
            .andExpect(view().name("articles/detail"))
            .andExpect(model().attributeExists("article"));


}

}
