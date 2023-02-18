package org.swyg.greensumer.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.swyg.greensumer.domain.UserEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails{
    private Long id;
    private String username;
    private String password;
    private String email;
    private String fullname;
    private String nickname;
    private boolean gender;
    private String roles;

    @JsonSerialize(using = LocalDateSerializer.class) @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birth;
    @JsonSerialize(using = LocalDateTimeSerializer.class) @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime registeredAt;
    @JsonSerialize(using = LocalDateTimeSerializer.class) @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;
    @JsonSerialize(using = LocalDateTimeSerializer.class) @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime deletedAt;

    public static User fromEntity(UserEntity entity){
        System.out.println(entity.getRoles());
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getEmail(),
                entity.getFullname(),
                entity.getNickname(),
                entity.isGender(),
                entity.getRoles().toString(),
                entity.getBirth(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRoles()));
    }

    @Override @JsonIgnore public boolean isAccountNonExpired() { return this.deletedAt == null; }
    @Override @JsonIgnore public boolean isAccountNonLocked() { return this.deletedAt == null; }
    @Override @JsonIgnore public boolean isCredentialsNonExpired() { return this.deletedAt == null; }
    @Override @JsonIgnore public boolean isEnabled() { return this.deletedAt == null; }

}
