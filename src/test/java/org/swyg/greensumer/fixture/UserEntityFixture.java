package org.swyg.greensumer.fixture;

import org.swyg.greensumer.domain.UserEntity;

public class UserEntityFixture {
    public static UserEntity get(String username, String password, String nickname, String email){
        return UserEntity.of(
                username,
                password,
                email,
                nickname
        );
    }

}
