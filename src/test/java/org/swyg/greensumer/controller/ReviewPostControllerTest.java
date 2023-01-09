package org.swyg.greensumer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.swyg.greensumer.domain.ProductEntity;
import org.swyg.greensumer.domain.ReviewPostEntity;
import org.swyg.greensumer.domain.StoreEntity;
import org.swyg.greensumer.domain.UserEntity;
import org.swyg.greensumer.domain.constant.UserRole;
import org.swyg.greensumer.dto.*;
import org.swyg.greensumer.dto.request.ReviewPostCreateRequest;
import org.swyg.greensumer.dto.request.ReviewPostModifyRequest;
import org.swyg.greensumer.dto.response.ProductResponse;
import org.swyg.greensumer.dto.response.ReviewCommentResponse;
import org.swyg.greensumer.dto.response.ReviewPostWithCommentResponse;
import org.swyg.greensumer.dto.response.UserResponse;
import org.swyg.greensumer.exception.ErrorCode;
import org.swyg.greensumer.exception.GreenSumerBackApplicationException;
import org.swyg.greensumer.fixture.ProductEntityFixture;
import org.swyg.greensumer.fixture.ReviewPostEntityFixture;
import org.swyg.greensumer.fixture.StoreEntityFixture;
import org.swyg.greensumer.fixture.UserEntityFixture;
import org.swyg.greensumer.service.ReviewCommentService;
import org.swyg.greensumer.service.ReviewPostService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("View 컨트롤러 - 후기 게시판")
@AutoConfigureMockMvc
@SpringBootTest
class ReviewPostControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;

    @MockBean private ReviewPostService reviewPostService;
    @MockBean private ReviewCommentService reviewCommentService;

    ReviewPostControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper objectMapper
    ) {
        this.mvc = mvc;
        this.objectMapper = objectMapper;
    }

    @DisplayName("[view][GET] 후기 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingReviewPosts_thenReturnsReviewPosts() throws Exception {
        // Given
        given(reviewPostService.list(any(Pageable.class))).willReturn(Page.empty());

        mvc.perform(get("/api/v1/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        // When & Then
        then(reviewPostService).should().list(any(Pageable.class));
    }

    @DisplayName("[view][GET] 후기 게시글 페이지 - 정상 호출")
    @WithMockUser
    @Test
    void givenNothing_whenRequestingReviewPost_thenReturnsReviewPost() throws Exception {
        // Given
        Integer postId = 1;
        given(reviewPostService.getPostAndComments(eq(postId), any())).willReturn(createReviewPostWithComments());

        mvc.perform(get("/api/v1/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        // When & Then
        then(reviewPostService).should().getPostAndComments(eq(postId), any());
    }

    @DisplayName("[view][GET] 후기 게시글 페이지 - 로그인하지 않은 경우")
    @WithAnonymousUser
    @Test
    void givenNothing_whenRequestingReviewPost_thenReturnsNothing() throws Exception {
        // Given
        Integer postId = 1;

        mvc.perform(get("/api/v1/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        // When & Then
        then(reviewPostService).shouldHaveNoInteractions();
    }

    @DisplayName("[view][GET] 후기 자신이 작성한 게시글 리스트 페이지 - 정상호출")
    @WithMockUser
    @Test
    void givenNothing_whenRequestingReviewPost_thenReturnsMyReviewPostList() throws Exception {
        //when
        when(reviewPostService.mylist(any(), any())).thenReturn(Page.empty());

        mvc.perform(get("/api/v1/posts/my")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        then(reviewPostService).should().mylist(any(), any());
    }

    @DisplayName("[view][GET] 후기 자신이 작성한 게시글 리스트 페이지 - 정상호출")
    @WithAnonymousUser
    @Test
    void givenNothing_whenRequestingMyReviewPost_thenReturnsNothing() throws Exception {
        mvc.perform(get("/api/v1/posts/my")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());

        then(reviewPostService).shouldHaveNoInteractions();
    }

    @DisplayName("[view][POST] 후기 게시글 생성 - 정상호출")
    @WithMockUser
    @Test
    void givenReviewPostInfoAndUser_whenRequestingCreatePost_thenSaveReviewPost() throws Exception {
        Integer productId = 1;
        String title = "title";
        String content = "content";
        String hashtag = "hashtag";
        String image = "image";

        mvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(ReviewPostCreateRequest.of(productId, title, content, hashtag, image)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[view][POST] 후기 게시글 생성 - 로그인하지 않은경우")
    @WithAnonymousUser
    @Test
    void givenReviewPostInfoAndNotLogin_whenRequestingCreatePost_thenNothing() throws Exception {
        Integer productId = 1;
        String title = "title";
        String content = "content";
        String hashtag = "hashtag";
        String image = "image";

        mvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(ReviewPostCreateRequest.of(productId, title, content, hashtag, image)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    

    private ReviewPostWithCommentResponse createReviewPostWithCommentsResponse() {
        return new ReviewPostWithCommentResponse(
                1,
                "title",
                "content",
                "hashtag",
                "imagePath",
                createProductResponse(),
                createUserResponse(),
                Set.of(createReviewCommentResponse()),
                Timestamp.from(Instant.now()),
                null,
                null
        );
    }

    private ReviewPostWithComment createReviewPostWithComments() {
        return new ReviewPostWithComment(
                1,
                "title",
                "content",
                "hasttag",
                "imagepath",
                createProduct(),
                createUser(),
                Set.of(createReviewComment()),
                Timestamp.from(Instant.now()),
                null,
                null
        );
    }

    private ReviewComment createReviewComment() {
        return new ReviewComment(
                1,
                "content",
                createUser(),
                createReviewPost(),
                Timestamp.from(Instant.now()),
                null,
                null
        );
    }

    private ReviewPost createReviewPost() {
        return new ReviewPost(
                1,
                "title",
                "content",
                "hashtag",
                "imagePath",
                createProduct(),
                createUser(),
                Timestamp.from(Instant.now()),
                null,
                null
        );
    }

    private User createUser() {
        return new User(
                1,
                "username",
                "password",
                "email",
                "nickname",
                "address",
                "lat",
                "lng",
                UserRole.USER,
                Timestamp.from(Instant.now()),
                null,
                null
        );
    }

    private Product createProduct() {
        return new Product(
                1,
                "name",
                2000,
                3,
                "description",
                "image",
                Timestamp.from(Instant.now()),
                null,
                null
        );
    }

    private ReviewCommentResponse createReviewCommentResponse() {
        return new ReviewCommentResponse(
                1,
                1,
                "username",
                "content",
                Timestamp.from(Instant.now()),
                null,
                null
        );
    }

    private UserResponse createUserResponse() {
        return new UserResponse(
                1,
                "username",
                "nickname",
                "user@email.com",
                UserRole.USER
        );
    }

    private ProductResponse createProductResponse() {
        return new ProductResponse(
                1,
                "name",
                3000,
                5,
                "description",
                "image"
        );
    }

}