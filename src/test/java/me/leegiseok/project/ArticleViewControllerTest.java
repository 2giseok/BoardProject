package me.leegiseok.project;

import me.leegiseok.project.config.SecurityConfig;
import me.leegiseok.project.config.jwt.JwtAuthFilter;
import me.leegiseok.project.config.jwt.JwtTokenProvider;
import me.leegiseok.project.controller.ArticleViewController;
import me.leegiseok.project.domain.Article;
import me.leegiseok.project.service.ArticleService;
import me.leegiseok.project.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;


import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = ArticleViewController.class,
excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = ".*SecurityConfig.*"
))
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
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
    Article post = Article.builder().title("제목").content("내용").author("leegiseok").build();
    given(articleService.findAll())
            .willReturn(List.of(post));


   mockMvc.perform(get("/articles")
                   .with(user("testuser").roles("USER")))
           .andExpect(status().isOk())

           .andExpect(view().name("articles/list"))
           .andExpect(model().attributeExists("articles"));



}

@Test
    @DisplayName("글 조회")
    void detailArticle()  {

}

}
