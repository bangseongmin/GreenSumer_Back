package org.swyg.greensumer.domain;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

@Getter
@Entity
@Table(name = "product", indexes = {
        @Index(name = "product_name_idx", columnList = "name")
})
@SQLDelete(sql = "UPDATE \"product\" SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class ProductEntity {
    @Setter @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity store;

    @Setter @Column(name = "name", length = 50) private String name;

    @Setter @Column(name = "price") private int price;
    @Setter @Column(name = "stock") private int stock;

    @Setter @Column(name = "description", columnDefinition = "TEXT") private String description;
    @Setter @Column(name = "image") private String image;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    void registeredAt() { this.registeredAt = Timestamp.from(Instant.now()); }

    @PreUpdate
    void updatedAt() { this.updatedAt = Timestamp.from(Instant.now());}

    public ProductEntity(){}

    public static ProductEntity of(StoreEntity storeEntity, String name, int price, int stock, String description, String image) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setStore(storeEntity);
        productEntity.setName(name);
        productEntity.setPrice(price);
        productEntity.setStock(stock);
        productEntity.setDescription(description);
        productEntity.setImage(image);

        return productEntity;
    }

    public static ProductEntity of(Integer id, StoreEntity storeEntity, String name, int price, int stock, String description, String image) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(id);
        productEntity.setStore(storeEntity);
        productEntity.setName(name);
        productEntity.setPrice(price);
        productEntity.setStock(stock);
        productEntity.setDescription(description);
        productEntity.setImage(image);

        return productEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductEntity that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
