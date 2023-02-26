package org.swyg.greensumer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.swyg.greensumer.domain.StoreImageEntity;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreImage {
    private Long id;
    private String username;
    private String originFilename;
    private String savedFilename;
    private byte[] imageData;

    public static StoreImage fromEntity(StoreImageEntity entity) {
        return StoreImage.builder()
                .id(entity.getId())
                .originFilename(entity.getOriginFilename())
                .imageData(entity.getImageData())
                .build();
    }

}
