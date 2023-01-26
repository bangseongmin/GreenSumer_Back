package org.swyg.greensumer.domain;

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
@Table(name = "review_post")
@SQLDelete(sql = "UPDATE review_post SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class ReviewPostEntity extends PostEntity {

    @OneToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ToString.Exclude
    @OrderBy("registeredAt DESC")
    @OneToMany(mappedBy = "reviewPost", cascade = CascadeType.ALL)
    private Set<ReviewCommentEntity> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "review", orphanRemoval = true, cascade = {CascadeType.ALL})
    private Set<ImageEntity> images = new LinkedHashSet<>();

    @ToString.Exclude @OneToMany(fetch = FetchType.EAGER, mappedBy = "review", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<ReviewPostViewerEntity> viewer = new LinkedHashSet<>();

    public ReviewPostEntity() {}

    private ReviewPostEntity (ProductEntity product, UserEntity user, String title, String content) {
       this.product = product;
       this.user = user;
       this.title = title;
       this.content = content;
       this.views = 0;
    }

    public static ReviewPostEntity of(ProductEntity product, UserEntity user, String title, String content) {
        return new ReviewPostEntity(product, user, title, content);
    }

    public void addImage(ImageEntity image) {
        image.setReview(this);
        this.images.add(image);
    }

    public void addImages(Collection<ImageEntity> images) {
        images.forEach(e -> e.setReview(this));
        this.images.clear();
        this.images.addAll(images);
    }

    public void deleteImage(ImageEntity image) {
        image.setReview(this);
        this.images.remove(image);
    }

    public void deleteImages(Collection<ImageEntity> images) {
        images.forEach(e -> e.setReview(this));
        this.images.retainAll(images);
    }

    public void clearImages() {
        this.images.clear();
    }

    public void addViewer(ReviewPostViewerEntity reviewPostViewerEntity) {
        if(this.viewer.contains(reviewPostViewerEntity)){
            return;
        }
        reviewPostViewerEntity.setReview(this);
        this.viewer.add(reviewPostViewerEntity);
        this.views++;
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

    public void updateReviewPost(ProductEntity product, String title, String content) {
        this.product = product;
        this.title = title;
        this.content = content;
    }
}
