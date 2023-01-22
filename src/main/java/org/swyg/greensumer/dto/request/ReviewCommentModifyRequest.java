package org.swyg.greensumer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewCommentModifyRequest {
    private String content;

    public static ReviewCommentModifyRequest of(String content){
        return new ReviewCommentModifyRequest(content);
    }
}
