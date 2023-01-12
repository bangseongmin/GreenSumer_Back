package org.swyg.greensumer.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.swyg.greensumer.domain.constant.StoreType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Entity
@Table(name = "\"store\"")
@SQLDelete(sql = "UPDATE \"store\" SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class StoreEntity {

    @Setter @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ToString.Exclude
    @OneToMany(mappedBy = "store", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<SellerStoreEntity> sellerStores = new LinkedHashSet<>();

    @Setter @Column(name = "name", length = 30) private String name;

    @Setter @Column(name = "description", columnDefinition = "TEXT") private String description;

    @Setter @Column(name = "type") @Enumerated(EnumType.STRING) private StoreType storeType;

    @Setter @ToString.Exclude
    @OneToOne @JoinColumn(name = "address_id")
    private AddressEntity address;

    @Setter private String hours;
    @Setter private String logo;

    @Column(name = "registered_at") private Timestamp registeredAt;
    @Column(name = "updated_at") private Timestamp updatedAt;
    @Column(name = "deleted_at") private Timestamp deletedAt;

    @PrePersist void registeredAt() { this.registeredAt = Timestamp.from(Instant.now()); }
    @PreUpdate  void updatedAt() { this.updatedAt = Timestamp.from(Instant.now());}

    public void addSellerStore(SellerStoreEntity sellerStoreEntity){
        sellerStoreEntity.setStore(this);
        this.sellerStores.add(sellerStoreEntity);
    }

    public StoreEntity(){}

    public static StoreEntity of(String name, String description, AddressEntity address, String hours, String logo, StoreType storeType) {
        return StoreEntity.of(null, name, description, address, hours, logo, storeType);
    }

    public static StoreEntity of(Integer id, String name, String description, AddressEntity address, String hours, String logo, StoreType storeType) {
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setId(id);
        storeEntity.setName(name);
        storeEntity.setDescription(description);
        storeEntity.setAddress(address);
        storeEntity.setHours(hours);
        storeEntity.setLogo(logo);
        storeEntity.setStoreType(storeType);

        return storeEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreEntity that)) return false;
        return this.getId() != null && this.getId().equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
