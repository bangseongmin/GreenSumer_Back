package org.swyg.greensumer.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", indexes = {
        @Index(name = "username_idx", columnList = "username", unique = true),
        @Index(name = "email_idx", columnList = "email", unique = true),
        @Index(name = "nickname_idx", columnList = "nickname", unique = true)
})
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class UserEntity extends DateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", length = 50) private String username;
    @Column(name = "password") private String password;
    @Column(nullable = false, length = 100) private String email;
    @Column(nullable = false, length = 50) private String nickname;

    @OneToOne @JoinColumn(name = "address_id") AddressEntity addressEntity;

    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
