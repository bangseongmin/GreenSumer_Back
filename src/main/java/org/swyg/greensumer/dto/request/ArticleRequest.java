package org.swyg.greensumer.dto.request;

import org.swyg.greensumer.dto.ArticleDto;
import org.swyg.greensumer.dto.UserAccountDto;

public record ArticleRequest(
        String title,
        String content,
        String hashtag,
        String image
) {

    public static ArticleRequest of(String title, String content, String hashtag, String image) {
        return new ArticleRequest(title, content, hashtag, image);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto) {
        return ArticleDto.of(
                userAccountDto,
                title,
                content,
                hashtag,
                image
        );
    }

}
