package org.swyg.greensumer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.swyg.greensumer.domain.InterviewEntity;
import org.swyg.greensumer.domain.constant.UserRole;

import java.util.List;

public interface InterviewEntityRepository extends JpaRepository<InterviewEntity, Long> {
    Page<InterviewEntity> findAllByTarget(UserRole role, Pageable pageable);
}
