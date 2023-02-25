package org.swyg.greensumer.repository.images;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swyg.greensumer.domain.EventImageEntity;
import org.swyg.greensumer.domain.ReviewImageEntity;

import java.util.List;

public interface EventImageEntityRepository extends JpaRepository<EventImageEntity, Long> {
    List<EventImageEntity> findAllByIdIn(List<Long> ids);

}
