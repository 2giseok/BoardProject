package me.leegiseok.project.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import jakarta.persistence.*;
import lombok.*;
import me.leegiseok.project.config.JpaConFig;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Table(name  ="articles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause =  "deleted_at IS NULL")
@Builder
@AllArgsConstructor
public class Article extends BaseTimeEntity {





    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

@Column(name = "deleted_at")
    private LocalDateTime deletedAt;

@Column(name = "deleted_by", length = 100)
private  String deletedBy;

    @Column(nullable = false, columnDefinition = "TEXT")
    private  String content;
    @Column(nullable = false)
    private  String  title;
    @Column(nullable = false, updatable = false)
    private  String author;
    @Column(nullable = false)
    @Builder.Default
    private  boolean deleted = false;




@Builder
    public  Article(String title, String content, String author) {
        this.title= title;
        this.content=content;
        this.author=author;
    }



    public void update(String title, String content) {
        this.title= title;
        this.content = content;
    }
    public  void softDeleted(String by) {
    this.deleted = true;
    this.deletedAt=LocalDateTime.now();
        this.deletedBy = by;
    }

    public boolean isDeleted() {return deleted; }
}
