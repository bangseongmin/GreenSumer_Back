package org.swyg.greensumer.dto.response;

import java.util.Set;

public record ArticleResponseDto(
        ArticleWithCommentsResponse article,
        Set<ArticleCommentResponse> articleComments,
        Long totalCount
) {
    public static ArticleResponseDto of(ArticleWithCommentsResponse article, Set<ArticleCommentResponse> articleComments, Long totalCount) {
        return new ArticleResponseDto(article, articleComments, totalCount);
    }
}
