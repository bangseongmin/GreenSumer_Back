package org.swyg.greensumer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.swyg.greensumer.domain.constant.UserRole;
import org.swyg.greensumer.dto.User;

@Getter
@AllArgsConstructor
public class UserSignUpResponse {
    private Integer id;
    private String username;
    private String nickname;
    private String email;
    private String address;
    private Double lat;
    private Double lng;
    private UserRole role;

    public static UserSignUpResponse fromUser(User user){
        return new UserSignUpResponse(
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getAddress().getAddress(),
                user.getAddress().getLat(),
                user.getAddress().getLng(),
                user.getRole()
        );
    }

}
