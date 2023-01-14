package org.swyg.greensumer.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "product", indexes = {
        @Index(name = "product_name_idx", columnList = "name")
})
@SQLDelete(sql = "UPDATE \"product\" SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ToString.Exclude
    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<StoreProductEntity> storeProducts = new LinkedHashSet<>();

    @Column(name = "name", length = 50) private String name;

    @Column(name = "price") private int price;
    @Column(name = "stock") private int stock;

    @Column(name = "description", columnDefinition = "TEXT") private String description;

    @OrderBy("registeredAt ASC")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<ImageEntity> images = new LinkedHashSet<>();

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist void registeredAt() { this.registeredAt = Timestamp.from(Instant.now()); }

    @PreUpdate void updatedAt() { this.updatedAt = Timestamp.from(Instant.now());}

    public void addStoreProduct(StoreProductEntity storeProductEntity){
        storeProductEntity.setProduct(this);
        this.storeProducts.add(storeProductEntity);
    }

    public void clearStoreProduct(){
        this.storeProducts.clear();
    }

    public void addImage(ImageEntity image) {
        image.setProduct(this);
        this.images.add(image);
    }

    public void addImages(Collection<ImageEntity> images) {
        images.forEach(e -> e.setProduct(this));
        this.images.addAll(images);
    }
    public void deleteImage(ImageEntity image) {
        image.setProduct(this);
        this.images.remove(image);
    }
    public void deleteImages(Collection<ImageEntity> images) {
        images.forEach(e -> e.setProduct(this));
        this.images.retainAll(images);
    }
    public void clearImages() {
        this.images.clear();
    }

    public ProductEntity(){}

    private ProductEntity(String name, int price, int stock, String description) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    public static ProductEntity of(String name, int price, int stock, String description) {
        return new ProductEntity(name, price, stock, description);
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
