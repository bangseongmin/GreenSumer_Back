package org.swyg.greensumer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.swyg.greensumer.exception.ErrorCode;
import org.swyg.greensumer.exception.GreenSumerBackApplicationException;
import org.swyg.greensumer.service.StoreService;
import org.swyg.greensumer.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.swyg.greensumer.fixture.Fixtures.getStore;
import static org.swyg.greensumer.fixture.RequestFixture.*;

@DisplayName("View 컨트롤러 - 제로웨이스트 샵")
@ActiveProfiles("test")
@WebMvcTest(StoreController.class)
class StoreControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;

    @MockBean private StoreService storeService;
    @MockBean private UserService userService;

    StoreControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper objectMapper
    ) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
    }

    @DisplayName("[view][POST] 가게 생성 - 정상호출")
    @WithMockUser
    @Test
    void givenStoreInfo_whenRequestingCreateStore_thenNothing() throws Exception {
        mvc.perform(post("/api/v1/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getStoreCreateRequest()))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[view][POST] 가게 생성 - 로그인하지 않은경우")
    @WithAnonymousUser
    @Test
    void givenStoreInfo_whenRequestingCreateStoreNotLogin_thenThrowUnauthroizedException() throws Exception {
        mvc.perform(post("/api/v1/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getStoreCreateRequest()))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[view][POST] 가게 생성 - 이미 존재하는 가게명인 경우")
    @WithMockUser
    @Test
    void givenDuplicateStoreInfo_whenRequestingCreateStore_thenThrowConflictError() throws Exception {
        doThrow(new GreenSumerBackApplicationException(ErrorCode.DUPLICATED_STORE_NAME)).when(storeService).create(any(), any());

        mvc.perform(post("/api/v1/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getStoreCreateRequest()))
                ).andDo(print())
                .andExpect(status().isConflict());
    }

    @DisplayName("[view][PUT] 가게 정보 수정 - 정상호출")
    @WithMockUser
    @Test
    void givenStoreInfo_whenRequestingModifyStore_thenReturnReviewPost() throws Exception {
        when(storeService.modify(any(), any(), any())).thenReturn(getStore());

        mvc.perform(put("/api/v1/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getStoreModifyRequest()))
                        .with(csrf())
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[view][PUT] 가게 정보 수정 - 로그인하지 않은 경우")
    @WithAnonymousUser
    @Test
    void givenStoreInfo_whenRequestingModifyStoreNotLogin_thenThrowUnauthroizedException() throws Exception {
        when(storeService.modify(any(), any(), any())).thenReturn(getStore());

        mvc.perform(put("/api/v1/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getReviewPostModifyRequest()))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[view][PUT] 가게 정보 수정 - 본인에 가게가 아닌 경우")
    @WithAnonymousUser
    @Test
    void givenStoreInfo_whenRequestingModifyOtherStore_thenThrowInvalid_Permission_Error() throws Exception {
        doThrow(new GreenSumerBackApplicationException(ErrorCode.INVALID_PERMISSION)).when(storeService).modify(any(), any(), any());

        mvc.perform(put("/api/v1/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getReviewPostModifyRequest()))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[view][PUT] 가게 정보 수정 - 존재하는 가게가 아닌 경우")
    @WithMockUser
    @Test
    void givenStoreInfo_whenRequestingDeleteStore_thenThrowNotFoundException() throws Exception {
        doThrow(new GreenSumerBackApplicationException(ErrorCode.STORE_NOT_FOUND)).when(storeService).modify(any(), any(), any());

        mvc.perform(put("/api/v1/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getReviewPostModifyRequest()))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("[view][DELETE] 가게 정보 삭제 - 정상호출")
    @WithMockUser
    @Test
    void givenStoreId_whenRequestingDeleteStore_thenReturnReviewPost() throws Exception {
        doNothing().when(storeService).delete(any(), any());

        mvc.perform(delete("/api/v1/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[view][DELETE] 가게 정보 삭제 - 로그인하지 않은 경우")
    @WithAnonymousUser
    @Test
    void givenStoreId_whenRequestingDeleteStoreNotLogin_thenThrowUnauthroizedException() throws Exception {
        doNothing().when(storeService).delete(any(), any());

        mvc.perform(delete("/api/v1/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[view][DELETE] 가게 정보 삭제 - 본인에 가게가 아닌 경우")
    @WithAnonymousUser
    @Test
    void givenStoreId_whenRequestingDeleteOtherStore_thenThrowInvalid_Permission_Error() throws Exception {
        doThrow(new GreenSumerBackApplicationException(ErrorCode.INVALID_PERMISSION)).when(storeService).delete(any(), any());

        mvc.perform(delete("/api/v1/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getReviewPostModifyRequest()))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("[view][DELETE] 가게 정보 삭제 - 존재하는 가게가 아닌 경우")
    @WithMockUser
    @Test
    void givenStoreId_whenRequestingDeleteStore_thenThrowNotFoundException() throws Exception {
        doThrow(new GreenSumerBackApplicationException(ErrorCode.STORE_NOT_FOUND)).when(storeService).delete(any(), any());

        mvc.perform(delete("/api/v1/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getReviewPostModifyRequest()))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

}