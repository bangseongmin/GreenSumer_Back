package org.swyg.greensumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swyg.greensumer.domain.EventPostEntity;

public interface EventPostEntityRepository extends JpaRepository<EventPostEntity, Long> {
}
