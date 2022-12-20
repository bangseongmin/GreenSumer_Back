package org.swyg.greensumer.dto.response;

import org.swyg.greensumer.dto.ArticleDto;

import java.time.LocalDateTime;

public record ArticleResponse(
        Long id,
        String title,
        String content,
        String hashtag,
        String image,
        String email,
        LocalDateTime createdAt,
        String nickname
) {

    public static ArticleResponse of(Long id, String title, String content, String hashtag, String image, String email, LocalDateTime createdAt, String nickname) {
        return new ArticleResponse(id, title, content, hashtag, image, email, createdAt, nickname);
    }

    public static ArticleResponse from(ArticleDto dto){
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().username();
        }

        return new ArticleResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashtag(),
                dto.article_image(),
                dto.userAccountDto().email(),
                dto.createdAt(),
                nickname
        );
    }

}
