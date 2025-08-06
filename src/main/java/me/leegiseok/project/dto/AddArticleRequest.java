package me.leegiseok.project.dto;

import lombok.Getter;
import lombok.Setter;
import me.leegiseok.project.domain.Article;

@Getter
@Setter
public class AddArticleRequest {
    private  String title;
    private  String content;

    public Article toEntity(String author) {
        return new  Article(title,content,author);

    }

}
