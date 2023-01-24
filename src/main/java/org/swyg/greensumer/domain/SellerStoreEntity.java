package org.swyg.greensumer.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "seller_store")
@SQLDelete(sql = "UPDATE seller_store SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class SellerStoreEntity extends DateTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter @ManyToOne @JoinColumn(name = "store_id") private StoreEntity store;        // 양방향

    @Setter @ManyToOne @JoinColumn(name = "seller_id") private UserEntity seller;       // 단방향

    public SellerStoreEntity() {}

    private SellerStoreEntity(StoreEntity store, UserEntity userEntity) {
        this.store = store;
        this.seller = userEntity;
    }

    public static SellerStoreEntity of(StoreEntity store, UserEntity seller) {
        return new SellerStoreEntity(store, seller);
    }
}
