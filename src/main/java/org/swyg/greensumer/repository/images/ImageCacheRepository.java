package org.swyg.greensumer.repository.images;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.swyg.greensumer.domain.ImageEntity;
import org.swyg.greensumer.domain.constant.ImageType;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ImageCacheRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOperations;

    @PostConstruct
    public void init() {
        this.valueOperations = redisTemplate.opsForValue();
    }

    public void setImages(ImageType type, List<ImageEntity> images) {
        if (Objects.isNull(images) || Objects.isNull(type)) {
            log.error("Required subKey or value must not be null");
            return;
        }

        try {
            String key = getKey() + type;
            for (ImageEntity image : images) {
                valueOperations.set(key + image.getId(), image);
            }

            log.info("[ImageCacheRepository save - success]");
        } catch (Exception e) {
            log.error("[ImageCacheRepository save - error] {}", e.getMessage());
        }
    }

    public List<ImageEntity> getImages(ImageType type, List<Long> images) {
        String key = getKey() + type;

        List<ImageEntity> list = new ArrayList<>();

        for (Long id : images) {
            ImageEntity imageData = (ImageEntity) valueOperations.get(key + id);
            list.add(imageData);
        }

        return list;
    }

    public byte[] getImage(ImageType type, Long id) {
        String key = getKey() + type;
        return (byte[])valueOperations.get(key + id);
    }

    private String getKey() {
        return "IMAGE-";
    }
}