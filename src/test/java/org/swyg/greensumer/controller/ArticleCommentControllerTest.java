package org.swyg.greensumer.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.swyg.greensumer.dto.ArticleCommentDto;
import org.swyg.greensumer.dto.request.ArticleCommentRequest;
import org.swyg.greensumer.service.ArticleCommentService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled
@DisplayName("View 컨트롤러 - 댓글")
@WebMvcTest(ArticleCommentController.class)
class ArticleCommentControllerTest {

    private final MockMvc mvc;

    @MockBean private ArticleCommentService articleCommentService;


    ArticleCommentControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }


    @DisplayName("[view][POST] 댓글 등록 - 정상 호출")
    @Test
    void givenArticleCommentInfo_whenRequesting_thenSavesNewArticleComment() throws Exception {
        // Given
        long articleId = 1L;
        ArticleCommentRequest request = ArticleCommentRequest.of(articleId, "test comment");
        willDoNothing().given(articleCommentService).saveArticleComment(any(ArticleCommentDto.class));
        Gson gson = new Gson();
        String content = gson.toJson(request);

        // When & Then
        mvc.perform(
                post("/comments/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId));
        then(articleCommentService).should().saveArticleComment(any(ArticleCommentDto.class));
    }

    @DisplayName("[view][GET] 댓글 삭제 - 정상 호출")
    @Test
    void givenArticleCommentIdToDelete_whenRequesting_thenDeletesArticleComment() throws Exception {
        // Given
        long articleId = 1L;
        long articleCommentId = 1L;
        willDoNothing().given(articleCommentService).deleteArticleComment(articleCommentId);

        // When & Then
        mvc.perform(
                post("/comments/" + articleCommentId + "/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("articleId", String.valueOf(articleId))
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId));
        then(articleCommentService).should().deleteArticleComment(articleCommentId);
    }

    @DisplayName("[view][POST] 댓글 수정 - 정상 호출")
    @Test
    void givenArticleCommentInfo_whenRequesting_thenUpdateArticleComment() throws Exception {
        // Given
        long articleId = 1L;
        long articleCommentId = 1L;
        ArticleCommentRequest request = ArticleCommentRequest.of(articleId, "test comment");
        willDoNothing().given(articleCommentService).updateArticleComment(any(ArticleCommentDto.class));
        Gson gson = new Gson();
        String content = gson.toJson(request);

        // When & Then
        mvc.perform(
                post("/comments/" + articleCommentId + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("articleId", String.valueOf(articleId))
                        .content(content)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId));
        then(articleCommentService).should().updateArticleComment(any(ArticleCommentDto.class));
    }
}