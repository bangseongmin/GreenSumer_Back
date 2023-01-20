package org.swyg.greensumer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewCommentCreateRequest {
    private String content;

    public static ReviewCommentCreateRequest of(String content){
        return new ReviewCommentCreateRequest(content);
    }
}
