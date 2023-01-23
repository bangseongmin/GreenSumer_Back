package org.swyg.greensumer.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Entity
@Table(name = "post_viewer")
@SQLDelete(sql = "UPDATE post_viewer SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class ReviewPostViewerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter @ManyToOne @JoinColumn(name = "post_id") private ReviewPostEntity review;       // 양방향
    @Setter @ManyToOne @JoinColumn(name = "user_id") private UserEntity user;         // 단방향

    @Column(name = "registered_at") private Timestamp registeredAt;
    @Column(name = "deleted_at")    private Timestamp deletedAt;

    @PrePersist void registeredAt() { this.registeredAt = Timestamp.from(Instant.now()); }

    public ReviewPostViewerEntity() {}

    private ReviewPostViewerEntity(ReviewPostEntity reviewPostEntity, UserEntity userEntity) {
        this.review = reviewPostEntity;
        this.user = userEntity;
    }

    public static ReviewPostViewerEntity of(ReviewPostEntity reviewPostEntity, UserEntity userEntity) {
        return new ReviewPostViewerEntity(reviewPostEntity, userEntity);
    }
}
