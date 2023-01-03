package org.swyg.greensumer.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.swyg.greensumer.domain.constant.RoleType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "username", unique = true),
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
@SQLDelete(sql = "UPDATE \"user_account\" SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(nullable = false, length = 50) private String username;
    @Setter @Column(nullable = false) private String password;
    @Setter @Column(nullable = false, length = 100) private String email;
    @Setter @Column(nullable = false, length = 50) private String nickname;

    @Setter @Enumerated(EnumType.STRING) @Column(nullable = false) private RoleType role;

    @Setter private String lat;
    @Setter private String lng;

    @Column(name = "registered_at") private Timestamp registeredAt;
    @Column(name = "updated_at")    private Timestamp updatedAt;
    @Column(name = "deleted_at")    private Timestamp deletedAt;

    @PrePersist
    void registeredAt() { this.registeredAt = Timestamp.from(Instant.now()); }

    @PreUpdate
    void updatedAt() { this.updatedAt = Timestamp.from(Instant.now());}

    public static UserAccount of(String username, String password, String email, String nickname, RoleType role) {
        return UserAccount.of(username, password, email, nickname, role, null, null);
    }

    public static UserAccount of(String username, String password, String email, String nickname, RoleType role, String lat, String lng) {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(username);
        userAccount.setPassword(password);
        userAccount.setEmail(email);
        userAccount.setNickname(nickname);
        userAccount.setRole(role);
        userAccount.setLat(lat);
        userAccount.setLng(lng);
        return userAccount;
    }
}
