package org.swyg.greensumer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.swyg.greensumer.domain.constant.SearchType;
import org.swyg.greensumer.dto.ArticleDto;
import org.swyg.greensumer.dto.ArticleWithCommentsDto;
import org.swyg.greensumer.repository.ArticleRepository;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable){
        return Page.empty();
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto searchArticle(Long articleId){
        return null;
    }

    public void saveArticle(ArticleDto dto){

    }

    public void updateArticle(ArticleDto dto){

    }

    public void deleteArticle(long article_id){

    }

}
