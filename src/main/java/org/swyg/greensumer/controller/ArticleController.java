package org.swyg.greensumer.controller;

import lombok.RequiredArgsConstructor;
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
import org.swyg.greensumer.dto.response.ArticlesResponse;
import org.swyg.greensumer.service.ArticleService;
import org.swyg.greensumer.service.PaginationService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final PaginationService paginationService;

    @GetMapping
    public ArticlesResponse articles(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page<ArticleResponse> articles = articleService.searchArticles(searchType, searchValue, pageable).map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());

        return ArticlesResponse.of(articles, barNumbers, SearchType.values());
    }

    @GetMapping("/{article_id}")
    public ArticleWithCommentsResponse articles(@PathVariable Long article_id){
        // TODO: articleService.getArticleCount() 어떻게 전달할 것인지

        return ArticleWithCommentsResponse.from(articleService.searchArticle(article_id));
    }
}
