package me.leegiseok.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import me.leegiseok.project.domain.Article;

@Getter
@Setter
public class AddArticleRequest {
   @NotBlank private  String title;
    @NotBlank private   String content;

    public Article toEntity(String author) {
        return new  Article(title,content,author);

    }

}

