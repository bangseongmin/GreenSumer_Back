package org.swyg.greensumer.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity store;

    @Column(name = "name", length = 50) private String name;

    @Column(name = "price") private int price;
    @Column(name = "stock") private int stock;

    @Column(name = "description", columnDefinition = "TEXT") private String description;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<ImageEntity> productImages;

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

    private ProductEntity(StoreEntity store, String name, int price, int stock, String description) {
        this.store = store;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }

    public static ProductEntity of(StoreEntity store, String name, int price, int stock, String description) {
        return new ProductEntity(store, name, price, stock, description);
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
