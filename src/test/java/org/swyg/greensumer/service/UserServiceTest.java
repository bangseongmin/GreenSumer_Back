package org.swyg.greensumer.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.swyg.greensumer.domain.UserEntity;
import org.swyg.greensumer.dto.User;
import org.swyg.greensumer.exception.ErrorCode;
import org.swyg.greensumer.exception.GreenSumerBackApplicationException;
import org.swyg.greensumer.repository.UserCacheRepository;
import org.swyg.greensumer.repository.UserEntityRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.swyg.greensumer.fixture.Fixtures.*;
import static org.swyg.greensumer.fixture.RequestFixture.getUserSignUpRequest;

@DisplayName("비즈니스 로직 - 유저")
@TestPropertySource(properties = {"jwt.token.expired-time-ms=2500000", "jwt.secret-key=abcdeabcdeabcdeabcdeabcdeabcdeabcdeabcde222"})
@ExtendWith({MockitoExtension.class, SpringExtension.class})
class UserServiceTest {

    private UserService sut;

    @Mock
    private UserEntityRepository userEntityRepository;
    @Mock
    private VerificationService verificationService;
    @Mock
    private UserCacheRepository userCacheRepository;
    @Mock
    private BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    String secretKey;
    @Value("${jwt.token.expired-time-ms}")
    Long expiredTimeMs;

    @BeforeEach
    void setUp() {
        sut = new UserService(userEntityRepository, userCacheRepository, verificationService, encoder, secretKey, expiredTimeMs);
    }

    @DisplayName("유저정보를 입력하면, 회원가입이 성공한다.")
    @Test
    void givenUserInfo_whenRequestingSignUp_thenReturnUser() {
        // Given
        given(userEntityRepository.findByUsername(any())).willReturn(Optional.empty());
        given(userEntityRepository.save(any())).willReturn(createSignUpUserAccount());

        // When
        User user = sut.signup(getUserSignUpRequest());

        //Then
        assertThat(user)
                .hasFieldOrPropertyWithValue("username", getUserSignUpRequest().getUsername())
                .hasFieldOrPropertyWithValue("nickname", getUserSignUpRequest().getNickname())
                .hasFieldOrPropertyWithValue("email", getUserSignUpRequest().getEmail())
                .hasFieldOrPropertyWithValue("address", getUserSignUpRequest().getAddress())
                .hasFieldOrPropertyWithValue("lat", getUserSignUpRequest().getLat())
                .hasFieldOrPropertyWithValue("lng", getUserSignUpRequest().getLng());

        verify(userEntityRepository, times(1)).findByUsername(any());
        verify(userEntityRepository, times(1)).save(any());
    }

    @DisplayName("토큰이 없는 상태로 아이디와 비밀번호를 입력하면, 로그인할 수 있다.")
    @Test
    void givenUsernameAndPassword_whenRequestingLogin_thenReturnToken() {
        // Given
        given(userCacheRepository.getUser(any())).willReturn(Optional.empty());
        given(userEntityRepository.findByUsername(any())).willReturn(Optional.of(mock(UserEntity.class)));
        willDoNothing().given(userCacheRepository).setUser(any());
        given(encoder.matches(any(), any())).willReturn(true);

        // when
        Assertions.assertDoesNotThrow(() -> sut.login(getUsername(), getPassword()));

        // then
        verify(userCacheRepository, times(1)).getUser(any());
        verify(userEntityRepository, times(1)).findByUsername(any());
        verify(userCacheRepository, times(1)).setUser(any());
        verify(encoder, times(1)).matches(any(), any());
    }

    @DisplayName("토큰을 가진 유저가 로그인하는 경우")
    @Test
    void givenUsernameAndPassword_whenRequestingLoginWithToken_thenReturnToken() {
        // Given
        given(userCacheRepository.getUser(any())).willReturn(Optional.of(getUser()));
        willDoNothing().given(userCacheRepository).setUser(any());
        given(encoder.matches(any(), any())).willReturn(true);

        // when
        Assertions.assertDoesNotThrow(() -> sut.login(getUsername(), getPassword()));

        // then
        verify(userCacheRepository, times(1)).getUser(any());
        verify(userCacheRepository, times(1)).setUser(any());
        verify(encoder, times(1)).matches(any(), any());
    }

    @DisplayName("유저의 아이디가 중복된 경우 에러를 발생한다.")
    @Test
    void givenUsername_whenRequestingIsDuplicateUsername_thenThrowDuplicateException() {
        // Given
        given(userEntityRepository.findByUsername(any())).willReturn(Optional.of(mock(UserEntity.class)));

        // When
        GreenSumerBackApplicationException exception = assertThrows(GreenSumerBackApplicationException.class, () -> sut.existUsername(getUsername()));

        //Then
        assertEquals(exception.getErrorCode(), ErrorCode.DUPLICATED_USERNAME);
        verify(userEntityRepository, times(1)).findByUsername(getUsername());
    }

    @DisplayName("로그인시 존재하지 않는 아이디를 로그인한 경우")
    @Test
    void givenLoginInfo_whenRequestingLogin_thenThrowUserNotFoundException() {
        // Given
        given(userCacheRepository.getUser(any())).willReturn(Optional.empty());
        given(userEntityRepository.findByUsername(any())).willReturn(Optional.empty());


        // when
        GreenSumerBackApplicationException exception = assertThrows(GreenSumerBackApplicationException.class, () -> sut.login(getUsername(), getPassword()));

        //Then
        assertEquals(exception.getErrorCode(), ErrorCode.USER_NOT_FOUND);
        verify(userCacheRepository, times(1)).getUser(any());
        verify(userEntityRepository, times(1)).findByUsername(any());
    }

    @DisplayName("로그인시 패스워드가 일치하지 않는 경우")
    @Test
    void givenLoginInfo_whenRequestingLogin_thenThrowInvalidPasswordException() {
        // Given
        given(userCacheRepository.getUser(any())).willReturn(Optional.empty());
        given(userEntityRepository.findByUsername(any())).willReturn(Optional.of(mock(UserEntity.class)));
        given(encoder.matches(any(), any())).willReturn(false);

        // when
        GreenSumerBackApplicationException exception = assertThrows(GreenSumerBackApplicationException.class, () -> sut.login(getUsername(), getPassword()));

        //Then
        assertEquals(exception.getErrorCode(), ErrorCode.INVALID_PASSWORD);
        verify(userCacheRepository, times(1)).getUser(any());
        verify(userEntityRepository, times(1)).findByUsername(any());
        verify(encoder).matches(any(), any());
    }

    @DisplayName("아이디 찾기 정상 요청")
    @Test
    void givenEmailAndCode_whenRequestingFindUsername_thenReturnUser() {

    }

    @DisplayName("비밀번호 찾기 정상 요청")
    @Test
    void givenPasswordUpdateRequest_whenRequestingUpdatePassword_thenReturnNothing() {

    }


}