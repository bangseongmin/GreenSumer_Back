package org.swyg.greensumer.dto;

import org.springframework.data.domain.Page;
import org.swyg.greensumer.dto.response.ArticleResponse;

import java.util.List;

public record ArticlesResponse(
        Page<ArticleResponse> articles,
        List<Integer> barNumbers,
        Long count
) {
    public static ArticlesResponse of(Page<ArticleResponse> articles, List<Integer> barNumbers, Long count) {
        return new ArticlesResponse(articles, barNumbers, count);
    }
}
