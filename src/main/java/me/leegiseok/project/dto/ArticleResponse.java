package me.leegiseok.project.dto;

import lombok.Getter;
import me.leegiseok.project.domain.Article;

@Getter
public class ArticleResponse {
    private  final String  title;
    private  final  String content;
    private  final   String author;

    public  ArticleResponse(Article article) {
        this.title=article.getTitle();
        this.content=article.getContent();
        this.author=article.getAuthor();

    }


}
