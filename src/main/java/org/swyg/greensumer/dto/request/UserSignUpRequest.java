package org.swyg.greensumer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequest {
    private String username;
    private String password;
    private String nickname;
    private String email;

    public static UserSignUpRequest of(String username, String password, String email, String nickname) {
        return new UserSignUpRequest(username, password, email, nickname);
    }

}
