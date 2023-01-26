package org.swyg.greensumer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.swyg.greensumer.domain.EventPostEntity;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventPost {
    private Long id;
    private String title;
    private String content;
    private Integer views;
    private Product product;
    private User user;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static EventPost fromEntity(EventPostEntity entity){
        return new EventPost(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getViews(),
                Product.fromEntity(entity.getProduct()),
                User.fromEntity(entity.getUser()),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

}
