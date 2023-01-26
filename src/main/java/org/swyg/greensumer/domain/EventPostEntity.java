package org.swyg.greensumer.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "event_post")
@SQLDelete(sql = "UPDATE event_post SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class EventPostEntity extends PostEntity{

    @OneToOne @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ToString.Exclude @OrderBy("registeredAt DESC")
    @OneToMany(mappedBy = "eventPost", cascade = CascadeType.ALL)
    private Set<EventCommentEntity> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "event", orphanRemoval = true, cascade = {CascadeType.ALL})
    private Set<ImageEntity> images = new LinkedHashSet<>();

    @ToString.Exclude @OneToMany(fetch = FetchType.EAGER, mappedBy = "event", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<EventPostViewerEntity> viewer = new LinkedHashSet<>();

    protected EventPostEntity() {}

    @Builder
    private EventPostEntity(ProductEntity product, UserEntity user, String title, String content) {
        this.product = product;
        this.user = user;
        this.title = title;
        this.content = content;
        this.views = 0;
    }

    public static EventPostEntity of(ProductEntity product, UserEntity user, String title, String content) {
        return new EventPostEntity(product, user, title, content);
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
        this.views++;
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

    public void updateEvent(ProductEntity productEntity, String title, String content) {
        this.product = productEntity;
        this.title = title;
        this.content = content;
    }
}
