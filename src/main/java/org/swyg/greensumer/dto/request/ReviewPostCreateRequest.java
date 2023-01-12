package org.swyg.greensumer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPostCreateRequest {
    private Integer productId;
    private String title;
    private String content;
    private String imagePath;

    public static ReviewPostCreateRequest of(Integer productId, String title, String content, String imagePath) {
        return new ReviewPostCreateRequest(productId, title, content, imagePath);
    }
}
