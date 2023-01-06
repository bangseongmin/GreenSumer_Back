package org.swyg.greensumer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPostModifyRequest {
    private Integer productId;
    private String title;
    private String content;
    private String hashtag;
    private String imagePath;

    public static ReviewPostModifyRequest of(Integer productId, String title, String content, String hashtag, String imagePath) {
        System.out.println("kKKKKKKKKK");
        return new ReviewPostModifyRequest(productId, title, content, hashtag, imagePath);
    }
}
