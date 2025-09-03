package me.leegiseok.project.repository;

import me.leegiseok.project.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findByTitleContainingIgnoreCase(String q, org.springframework.data.domain.Pageable pageable) ;

    Optional<Article> findByIdAndDeletedFalse(Long id);

    Page<Article> findAllByDeletedFalse(Pageable pageable);

}
