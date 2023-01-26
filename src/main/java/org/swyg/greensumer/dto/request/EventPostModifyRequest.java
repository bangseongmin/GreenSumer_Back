package org.swyg.greensumer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventPostModifyRequest {
    private Long productId;
    private String title;
    private String content;
    private List<Long> images;

    public static EventPostModifyRequest of(Long productId, String title, String content, List<Long> images) {
        return new EventPostModifyRequest(productId, title, content, images);
    }
}