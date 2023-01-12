package org.swyg.greensumer.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

@Getter
@Table(name = "\"address\"")
@SQLDelete(sql = "UPDATE \"address\" SET deleted_at = NOW() where id=?")
@Entity
public class AddressEntity {

    @Setter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(columnDefinition = "TEXT", unique = true) private String address;
    @Setter @Column(columnDefinition = "TEXT", unique = true) private String roadname;

    @Setter @Column(name = "lat", unique = true) private Double latitude;
    @Setter @Column(name = "lng", unique = true) private Double longitude;

    @Setter @Column(name = "registered_at") private Timestamp registeredAt;
    @Setter @Column(name = "updated_at") private Timestamp updatedAt;
    @Setter @Column(name = "deleted_at") private Timestamp deletedAt;


    @PrePersist void registeredAt() { this.registeredAt = Timestamp.from(Instant.now()); }
    @PreUpdate  void updatedAt() { this.updatedAt = Timestamp.from(Instant.now());}

    public AddressEntity() {}

    private AddressEntity(Long id, String address, String roadname, Double latitude, Double longitude, Timestamp registeredAt, Timestamp updatedAt, Timestamp deletedAt) {
        this.id = id;
        this.address = address;
        this.roadname = roadname;
        this.latitude = latitude;
        this.longitude = longitude;
        this.registeredAt = registeredAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static AddressEntity of(String address, String roadname, Double latitude, Double longitude) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setAddress(address);
        addressEntity.setRoadname(roadname);
        addressEntity.setLatitude(latitude);
        addressEntity.setLongitude(longitude);
        return addressEntity;
    }

    public static AddressEntity of(Long id, String address, String roadname, Double latitude, Double longitude, Timestamp registeredAt, Timestamp updatedAt, Timestamp deletedAt) {
        return new AddressEntity(id, address, roadname, latitude, latitude, registeredAt, updatedAt, deletedAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressEntity that)) return false;
        return this.getId() != null && this.getId().equals(that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(this.getId()); }

}
