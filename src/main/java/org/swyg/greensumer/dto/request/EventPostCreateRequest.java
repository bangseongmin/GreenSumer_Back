package org.swyg.greensumer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventPostCreateRequest {
    private Long storeId;
    private String title;
    private String content;
    private List<Long> products;
    private List<Long> images;

    public static EventPostCreateRequest of(Long storeId, String title, String content, List<Long> products, List<Long> images) {
        return new EventPostCreateRequest(storeId, title, content, products, images);
    }
}
