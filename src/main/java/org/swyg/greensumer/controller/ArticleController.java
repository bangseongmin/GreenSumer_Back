package org.swyg.greensumer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.swyg.greensumer.domain.constant.SearchType;
import org.swyg.greensumer.dto.UserAccountDto;
import org.swyg.greensumer.dto.request.ArticleRequest;
import org.swyg.greensumer.dto.response.ArticleResponse;
import org.swyg.greensumer.dto.response.ArticleResponseDto;
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

    // TODO: articleService.getArticleCount() 어떻게 전달할 것인지
    @GetMapping("/{article_id}")
    public ArticleResponseDto articles(@PathVariable Long article_id){
        ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(articleService.searchArticleWithComments(article_id));

        return ArticleResponseDto.of(article, article.articleCommentsResponse(), articleService.getArticleCount());
    }

    @PostMapping("/create")
    public String postNewArticle(ArticleRequest articleRequest) {
        articleService.saveArticle(articleRequest.toDto(UserAccountDto.of("username", "asdf1234", "uno@mail.com", "Artist")));

        return "redirect:/articles";
    }

    @GetMapping ("/{articleId}/update")
    public ArticleResponse updateArticleView(@PathVariable Long articleId) {
        return ArticleResponse.from(articleService.searchArticle(articleId));
    }

    @PostMapping ("/{articleId}/update")
    public String updateArticle(@PathVariable Long articleId, ArticleRequest articleRequest) {
        // TODO: 인증 정보를 넣어줘야 한다.
        articleService.updateArticle(articleId, articleRequest.toDto(UserAccountDto.of("username", "asdf1234", "uno@mail.com", "Artist")));

        return "redirect:/articles/" + articleId;
    }

    @PostMapping ("/{articleId}/delete")
    public String deleteArticle(@PathVariable Long articleId) {
        // TODO: 인증 정보를 넣어줘야 한다.
        articleService.deleteArticle(articleId);
        return "redirect:/articles";
    }

}
