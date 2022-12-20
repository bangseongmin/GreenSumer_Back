package org.swyg.greensumer.dto;

import org.swyg.greensumer.domain.UserAccount;
import org.swyg.greensumer.domain.constant.RoleType;

import java.time.LocalDateTime;

public record UserAccountDto(
        Long id,
        String username,
        String password,
        String email,
        String nickname,
        RoleType roleType,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
){
    public static UserAccountDto of(Long id, String username, String password, String email, String nickname, RoleType roleType, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new UserAccountDto(id, username, password, email, nickname, roleType, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static UserAccountDto from(UserAccount entity) {
        return new UserAccountDto(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getGrade(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    public UserAccount toEntity() {
        return UserAccount.of(
                username,
                password,
                email,
                nickname,
                RoleType.USER
        );
    }
}
