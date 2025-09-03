package me.leegiseok.project.service;


import lombok.RequiredArgsConstructor;
import me.leegiseok.project.domain.Article;
import me.leegiseok.project.dto.AddArticleRequest;
import me.leegiseok.project.dto.UpdateArticleRequest;
import me.leegiseok.project.repository.ArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ArticleService {

    private  final ArticleRepository articleRepository;
//저장
    @Transactional
    public Article save(AddArticleRequest request,String author) {
        return articleRepository.save(request.toEntity(author));
    }
    //글 목록 조회
    public List<Article> findAll() {
        return  articleRepository.findAll();

    }
    //상세 조회
    public  Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 글은 없습니다" + id));

    }
    //글 삭제
    @Transactional
    public void delete (Long id, String actor) {
        var a= findById(id);
        if(!a.getAuthor().equals(actor)) {
            throw new SecurityException("삭제 권한이 없습니다");
        }
       a.softDeleted(actor);
    }

public Page<Article> findPage(Pageable pageable) {
        return articleRepository.findAll(pageable);
}

@Transactional
    //글 수정

    public  Article update (Long id, UpdateArticleRequest request,String actor ) {
       var a= articleRepository.findById(id)
               .orElseThrow(() -> new IllegalArgumentException("게시물 없음"));

       if(!a.getAuthor().equals(actor)) {
           throw  new SecurityException("수정 권한이 없습니다");
       }
       a.update(request.title(), request.content());
       return a;
    }

    public  Page<Article> searchByTitle(String q, Pageable pageable){
        return  articleRepository.findByTitleContainingIgnoreCase(q, pageable);
    }
}
