package org.swyg.greensumer.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "store_product")
@SQLDelete(sql = "UPDATE store_product SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class StoreProductEntity extends DateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter @ManyToOne @JoinColumn(name = "store_id") private StoreEntity store;

    @Setter @ManyToOne @JoinColumn(name = "product_id") private ProductEntity product;

    public StoreProductEntity() {}

    private StoreProductEntity(StoreEntity store, ProductEntity product) {
        this.store = store;
        this.product= product;
    }

    public static StoreProductEntity of(StoreEntity store, ProductEntity product) {
        return new StoreProductEntity(store, product);
    }
}
