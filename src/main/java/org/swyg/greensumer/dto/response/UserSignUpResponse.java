package org.swyg.greensumer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.swyg.greensumer.dto.Address;
import org.swyg.greensumer.dto.User;

@Getter
@AllArgsConstructor
public class UserSignUpResponse {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String roles;

    public static UserSignUpResponse fromUser(User user){
        return new UserSignUpResponse(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getRoles()
        );
    }

}
