package me.leegiseok.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.leegiseok.project.domain.Article;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddArticleRequest {
   @NotBlank private  String title;
    @NotBlank private   String content;




    public Article toEntity(String author) {
        return Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();

    }

}

