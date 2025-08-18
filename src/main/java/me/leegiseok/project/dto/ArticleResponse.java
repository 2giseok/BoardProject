package me.leegiseok.project.dto;

import lombok.Getter;
import me.leegiseok.project.domain.Article;

import java.time.LocalDateTime;

@Getter
public class ArticleResponse {
    private  final Long id;
    private  final String  title;
    private  final  String content;
    private  final   String author;
    private  final LocalDateTime createdAt;

    public  ArticleResponse(Article article) {
        this.title=article.getTitle();
        this.content=article.getContent();
        this.author=article.getAuthor();
        this.id = article.getId();
        this.createdAt = article.getCreatedAt();
    }


}
