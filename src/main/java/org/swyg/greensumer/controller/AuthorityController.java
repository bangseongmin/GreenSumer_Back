package org.swyg.greensumer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swyg.greensumer.dto.User;
import org.swyg.greensumer.dto.request.UpdateUserRequest;
import org.swyg.greensumer.dto.response.Response;
import org.swyg.greensumer.dto.response.UpdateUserResponse;
import org.swyg.greensumer.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/update")
public class AuthorityController {

    private final UserService userService;

    @PutMapping("/user")
    public Response<UpdateUserResponse> updateUser(@RequestBody UpdateUserRequest request, Authentication authentication) {
        User user = userService.updateUserInfo(request, authentication.getName());
        return Response.success(UpdateUserResponse.fromUser(user));
    }

}
