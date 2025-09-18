package me.leegiseok.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.leegiseok.project.config.jwt.JwtAuthFilter;
import me.leegiseok.project.config.jwt.JwtTokenProvider;
import me.leegiseok.project.controller.ArticlesApiController;
import me.leegiseok.project.domain.Article;
import me.leegiseok.project.dto.AddArticleRequest;
import me.leegiseok.project.dto.UpdateArticleRequest;
import me.leegiseok.project.service.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers =  ArticlesApiController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ArticleControllerTest {
    @MockitoBean JwtTokenProvider jwtTokenProvider;
    @MockitoBean
    JwtAuthFilter jwtAuthFilter;

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    ArticleService articleService;
    @Autowired
    ObjectMapper objectMapper;
    private Principal mockPrincipal(String name) {
        return () -> name;
    }

    @Test
    @DisplayName("글 생성 201 ")
    void getArticle()throws  Exception {

        AddArticleRequest request = new AddArticleRequest("제목","내용");
        Article saved = Article.builder().title("제목").content("내용").build();
        given(articleService.save(any(AddArticleRequest.class),eq("Leegiseok")))
                .willReturn(saved);


        mockMvc.perform(post("/api/articles")
                .principal(mockPrincipal("Leegiseok"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"));



    }

    @Test
    @DisplayName("글 조회 200")
    void ArticleDetail() throws Exception {
        Article found =  Article.builder().title("제목").content("내용").build();
        given(articleService.findById(1L)).willReturn(found);
        mockMvc.perform(get("/api/articles/{id}",1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"));


    }

    @Test
    @DisplayName("글 수정 ")
    void updateArticle()throws  Exception {
        UpdateArticleRequest request = new UpdateArticleRequest("새 제목", " 새 내용");

        given(articleService.update(anyLong(),any(UpdateArticleRequest.class),anyString())).willReturn(Article.builder().title("새 제목").content("새 내용").build());

        mockMvc.perform(patch(  "/api/articles/{id}",1L)
                        .principal(() -> "Leegiseok")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("새 제목"))
                .andExpect(jsonPath("$.content").value("새 내용"));


    }

    @Test
    @DisplayName("글 삭제")
    void deleteArticle()throws  Exception {
        mockMvc.perform(delete("/api/articles/{id}",1L)
                .principal(mockPrincipal("Leegiseok")))
                .andExpect(status().isNoContent());

    }
    @Test
    @DisplayName("빈값 검증 실패 ")
    void  create400error() throws Exception {
        var request = new UpdateArticleRequest("","");

        mockMvc.perform(patch("/api/articles/{id}",1L)
                .principal(() -> "Leegiseok")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(articleService, never()).update(anyLong(),any(UpdateArticleRequest.class),anyString());



    }




}
