package org.swyg.greensumer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.swyg.greensumer.dto.EventPostWithComment;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventPostWithCommentResponse {
    private Long id;
    private String title;
    private String content;
    private Integer views;
    private ProductResponse product;
    private UserResponse user;
    private Set<EventCommentResponse> reviewComments;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static EventPostWithCommentResponse fromEventPostWithComment(EventPostWithComment postWithComment) {
        return new EventPostWithCommentResponse(
                postWithComment.getId(),
                postWithComment.getTitle(),
                postWithComment.getContent(),
                postWithComment.getViews(),
                ProductResponse.fromProduct(postWithComment.getProduct()),
                UserResponse.fromUser(postWithComment.getUser()),
                postWithComment.getReviewComments().stream()
                        .map(EventCommentResponse::fromEventComment)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                postWithComment.getRegisteredAt(),
                postWithComment.getUpdatedAt(),
                postWithComment.getDeletedAt()
        );
    }
}
