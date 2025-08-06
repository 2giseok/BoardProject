package me.leegiseok.project.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;


    private  String content;
    private  String  title;

    private  String author;

    public  Article(String title, String content, String author) {
        this.title= title;
        this.content=content;
        this.author=author;

    }

    public void update(String title, String content) {
        this.title= title;
        this.content = content;
    }
}
