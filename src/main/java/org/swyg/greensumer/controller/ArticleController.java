package org.swyg.greensumer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.swyg.greensumer.domain.constant.SearchType;
import org.swyg.greensumer.dto.response.ArticleResponse;
import org.swyg.greensumer.dto.response.ArticleWithCommentsResponse;
import org.swyg.greensumer.service.ArticleService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

    private ArticleService articleService;

    @GetMapping
    public Page<ArticleResponse> articles(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){

        return articleService.searchArticles(searchType, searchValue, pageable).map(ArticleResponse::from);
    }

    @GetMapping("/{article_id}")
    public ArticleWithCommentsResponse articles(@PathVariable Long article_id){

        return ArticleWithCommentsResponse.from(articleService.searchArticle(article_id));
    }
}
