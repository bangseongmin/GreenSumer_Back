package org.swyg.greensumer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.swyg.greensumer.dto.Image;
import org.swyg.greensumer.dto.SellerStore;
import org.swyg.greensumer.dto.Store;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponse {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String hours;
    private String phone;
    private String url;
    private Double lat;
    private Double lng;
    private Set<Image> images;

    public static StoreResponse fromStore(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getName(),
                store.getDescription(),
                store.getAddress().getAddress(),
                store.getHours(),
                store.getPhone(),
                store.getUrl(),
                store.getAddress().getLat(),
                store.getAddress().getLng(),
                store.getLogos()
        );
    }

    public static StoreResponse fromSellerStore(SellerStore sellerStore) {
        return new StoreResponse(
                sellerStore.getStore().getId(),
                sellerStore.getStore().getName(),
                sellerStore.getStore().getDescription(),
                sellerStore.getStore().getAddress().getAddress(),
                sellerStore.getStore().getHours(),
                sellerStore.getStore().getPhone(),
                sellerStore.getStore().getUrl(),
                sellerStore.getStore().getAddress().getLat(),
                sellerStore.getStore().getAddress().getLng(),
                sellerStore.getStore().getLogos()
        );
    }
}
