package org.swyg.greensumer.dto.response;

import org.springframework.data.domain.Page;
import org.swyg.greensumer.domain.constant.SearchType;

import java.util.List;

public record ArticlesResponse(
        Page<ArticleResponse> articles,
        List<Integer> barNumbers,
        SearchType[] searchTypes
) {
    public static ArticlesResponse of(Page<ArticleResponse> articles, List<Integer> barNumbers, SearchType[] searchTypes) {
        return new ArticlesResponse(articles, barNumbers, searchTypes);
    }
}
