package org.swyg.greensumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swyg.greensumer.domain.ReviewPostViewerEntity;

import java.util.Optional;

public interface ReviewPostViewerEntityRepository extends JpaRepository<ReviewPostViewerEntity, Long> {


    Optional<ReviewPostViewerEntity> findByReview_IdAndUser_Id(Long reviewPostId, Long userId);
}
