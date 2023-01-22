package org.swyg.greensumer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewPostModifyRequest {
    private Integer productId;
    private String title;
    private String content;
    private List<Integer> images;

    public static ReviewPostModifyRequest of(Integer productId, String title, String content, List<Integer> images) {
        return new ReviewPostModifyRequest(productId, title, content, images);
    }
}
