package org.swyg.greensumer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.*;

@AllArgsConstructor
@Getter
@Entity
@Table(name = "event_post")
@SQLDelete(sql = "UPDATE event_post SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class EventPostEntity extends PostEntity {

    @OneToMany(mappedBy = "eventPost")
    private Set<ProductEntity> products = new LinkedHashSet<>();

    @ToString.Exclude @OrderBy("registeredAt DESC")
    @OneToMany(mappedBy = "eventPost")
    private Set<EventCommentEntity> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "event", orphanRemoval = true, cascade = {CascadeType.ALL})
    private Set<ImageEntity> images = new LinkedHashSet<>();

    @ToString.Exclude @OneToMany(fetch = FetchType.EAGER, mappedBy = "event", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<EventPostViewerEntity> viewer = new LinkedHashSet<>();

    protected EventPostEntity() {}

    private EventPostEntity(UserEntity user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
    }

    @Builder
    public static EventPostEntity of(UserEntity user, String title, String content) {
        return new EventPostEntity(user, title, content);
    }

    public void addImages(Collection<ImageEntity> images) {
        images.forEach(e -> e.setEvent(this));
        this.images.clear();
        this.images.addAll(images);
    }

    public void addViewer(EventPostViewerEntity eventPostViewerEntity) {
        if(this.viewer.contains(eventPostViewerEntity)){
            return;
        }

        eventPostViewerEntity.setEvent(this);
        this.viewer.add(eventPostViewerEntity);
    }

    public void addProducts(Collection<ProductEntity> productEntities) {
        productEntities.forEach(p -> p.setEventPost(this));
        this.products.clear();
        this.products.addAll(productEntities);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof EventPostEntity that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    public void updateEventPost(List<ProductEntity> productEntities, String title, String content) {
        this.title = title;
        this.content = content;

        addProducts(productEntities);
    }

    public void clear() {
        this.images.clear();
        this.viewer.clear();
        this.products.clear();
    }
}
