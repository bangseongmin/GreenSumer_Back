package org.swyg.greensumer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.swyg.greensumer.dto.Store;
import org.swyg.greensumer.dto.StoreImage;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreCreateResponse {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String hours;
    private Double lat;
    private Double lng;
    private Set<StoreImage> images;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static StoreCreateResponse fromStore(Store store) {
        return new StoreCreateResponse(
                store.getId(),
                store.getName(),
                store.getDescription(),
                store.getAddress().getAddress(),
                store.getHours(),
                store.getAddress().getLat(),
                store.getAddress().getLng(),
                store.getLogos(),
                store.getRegisteredAt(),
                store.getUpdatedAt(),
                store.getDeletedAt()
        );
    }
}
