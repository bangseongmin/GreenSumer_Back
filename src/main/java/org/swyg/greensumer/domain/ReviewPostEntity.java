package org.swyg.greensumer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@AllArgsConstructor
@Getter
@Entity
@Table(name = "review_post")
@SQLDelete(sql = "UPDATE review_post SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class ReviewPostEntity extends PostEntity {

    @OneToMany(mappedBy = "reviewPost")
    private Set<ProductEntity> products = new LinkedHashSet<>();

    @ToString.Exclude
    @OrderBy("registeredAt DESC")
    @OneToMany(mappedBy = "reviewPost", cascade = CascadeType.ALL)
    private Set<ReviewCommentEntity> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "review", orphanRemoval = true, cascade = {CascadeType.ALL})
    private Set<ImageEntity> images = new LinkedHashSet<>();

    @ToString.Exclude @OneToMany(fetch = FetchType.EAGER, mappedBy = "review", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<ReviewPostViewerEntity> viewer = new LinkedHashSet<>();

    @ToString.Exclude @OneToMany(fetch = FetchType.EAGER, mappedBy = "review", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<ReviewPostLikeEntity> likes = new LinkedHashSet<>();

    public ReviewPostEntity() {}

    private ReviewPostEntity (UserEntity user, String title, String content) {
       this.user = user;
       this.title = title;
       this.content = content;
       this.views = 0;
       this.like = 0;
    }

    public static ReviewPostEntity of(UserEntity user, String title, String content) {
        return new ReviewPostEntity(user, title, content);
    }

    public void addImages(Collection<ImageEntity> images) {
        images.forEach(e -> e.setReview(this));
        this.images.clear();
        this.images.addAll(images);
    }

    public void addViewer(ReviewPostViewerEntity reviewPostViewerEntity) {
        if(this.viewer.contains(reviewPostViewerEntity)){
            return;
        }
        reviewPostViewerEntity.setReview(this);
        this.viewer.add(reviewPostViewerEntity);
        this.views++;
    }

    public void addProducts(Collection<ProductEntity> productEntities) {
        productEntities.forEach(p -> p.setReviewPost(this));
        this.products.clear();
        this.products.addAll(productEntities);
    }

    public void addLikes(ReviewPostLikeEntity reviewPostLikeEntity){
        if(this.likes.remove(reviewPostLikeEntity)) {
            this.like--;
            return;
        }

        reviewPostLikeEntity.setReview(this);
        this.likes.add(reviewPostLikeEntity);
        this.like++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof ReviewPostEntity that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    public void updateReviewPost(String title, String content, Collection<ProductEntity> productEntities) {
        this.title = title;
        this.content = content;
        addProducts(productEntities);
    }

    public void clear() {
        this.products.clear();
        this.images.clear();
        this.viewer.clear();
    }
}
