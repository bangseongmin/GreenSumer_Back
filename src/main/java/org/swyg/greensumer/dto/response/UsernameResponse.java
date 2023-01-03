package org.swyg.greensumer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsernameResponse {
    String username;

    public static UsernameResponse of(String username) {
        return new UsernameResponse(username);
    }
}
