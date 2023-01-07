package org.swyg.greensumer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.swyg.greensumer.exception.ErrorCode;
import org.swyg.greensumer.exception.GreenSumerBackApplicationException;
import org.swyg.greensumer.service.UserService;
import org.swyg.greensumer.service.VerificationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.swyg.greensumer.fixture.Fixtures.getToken;
import static org.swyg.greensumer.fixture.Fixtures.getUser;
import static org.swyg.greensumer.fixture.RequestFixture.getUserLoginRequest;
import static org.swyg.greensumer.fixture.RequestFixture.getUserSignUpRequest;

@DisplayName("View 컨트롤러 - 유저")
@ActiveProfiles("test")
@WebMvcTest(UserController.class)
class UserControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;

    @MockBean private UserService userService;
    @MockBean private VerificationService verificationService;

    public UserControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper objectMapper
    ) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
    }

    @DisplayName("[view][POST] 회원가입 요청 - 정상 호출")
    @Test
    void givenUserInfo_whenRequestingSignUp_thenReturnUserInfo() throws Exception{
        when(userService.signup(any())).thenReturn(getUser());

        mvc.perform(post("/api/v1/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getUserSignUpRequest()))
                )
                .andExpect(status().isOk());
    }

    @DisplayName("[view][POST] 회원가입 요청 - 이미 존재하는 아이디인 경우")
    @Test
    void givenUserInfo_whenRequestingSignUp_thenThrowDuplicatedUsernameException() throws Exception{
        when(userService.signup(any())).thenThrow(new GreenSumerBackApplicationException(ErrorCode.DUPLICATED_USERNAME));

        mvc.perform(post("/api/v1/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getUserSignUpRequest()))
                )
                .andExpect(status().isConflict());
    }

    @DisplayName("[view][POST] 로그인 요청 - 정상 호출")
    @Test
    void givenUserInfo_whenRequestingLogin_thenReturnToken() throws Exception{
        when(userService.login(any(), any())).thenReturn(getToken());

        mvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getUserLoginRequest()))
                )
                .andExpect(status().isOk());
    }

    @DisplayName("[view][POST] 로그인 요청 - 회원가입이 되지 않은 유저인경우")
    @Test
    void givenUserInfo_whenRequestingLogin_thenThrowUserNotFoundException() throws Exception{
        when(userService.login(any(), any())).thenThrow(new GreenSumerBackApplicationException(ErrorCode.USER_NOT_FOUND));

        mvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getUserLoginRequest()))
                )
                .andExpect(status().isNotFound());
    }

    @DisplayName("[view][POST] 로그인 요청 - 패스워드가 일치하지 않는 경우")
    @Test
    void givenUserInfo_whenRequestingLogin_thenThrowInvalidPasswordException() throws Exception{
        when(userService.login(any(), any())).thenThrow(new GreenSumerBackApplicationException(ErrorCode.INVALID_PASSWORD));

        mvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getUserLoginRequest()))
                )
                .andExpect(status().isUnauthorized());
    }

    

}