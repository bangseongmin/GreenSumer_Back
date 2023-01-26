package org.swyg.greensumer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.swyg.greensumer.domain.EventPostEntity;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventPostWithComment {
    private Long id;
    private String title;
    private String content;
    private Integer views;
    private Product product;
    private User user;
    private Set<EventComment> reviewComments;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static EventPostWithComment fromEntity(EventPostEntity entity) {
        return new EventPostWithComment(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getViews(),
                Product.fromEntity(entity.getProduct()),
                User.fromEntity(entity.getUser()),
                entity.getComments().stream()
                        .map(EventComment::fromEntity)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
