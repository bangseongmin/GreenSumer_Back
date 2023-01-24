package org.swyg.greensumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.swyg.greensumer.domain.UserEntity;
import org.swyg.greensumer.domain.constant.UserRole;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private String nickname;
    private Address address;
    private String roles;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static User fromEntity(UserEntity entity){
        System.out.println(entity.getRoles());
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getEmail(),
                entity.getNickname(),
                Address.fromEntity(entity.getAddressEntity()),
                entity.getRoles().toString(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRoles().toString()));
    }

    @Override @JsonIgnore public boolean isAccountNonExpired() { return this.deletedAt == null; }
    @Override @JsonIgnore public boolean isAccountNonLocked() { return this.deletedAt == null; }
    @Override @JsonIgnore public boolean isCredentialsNonExpired() { return this.deletedAt == null; }
    @Override @JsonIgnore public boolean isEnabled() { return this.deletedAt == null; }

}
