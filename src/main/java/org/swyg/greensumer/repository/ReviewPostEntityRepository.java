package org.swyg.greensumer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.swyg.greensumer.domain.ReviewPostEntity;

public interface ReviewPostEntityRepository extends JpaRepository<ReviewPostEntity, Long> {

    Page<ReviewPostEntity> findAllByUser_Id(Long userId, Pageable pageable);
}
