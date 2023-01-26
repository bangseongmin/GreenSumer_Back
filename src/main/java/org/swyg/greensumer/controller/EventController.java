package org.swyg.greensumer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.swyg.greensumer.dto.ReviewPost;
import org.swyg.greensumer.dto.ReviewPostWithComment;
import org.swyg.greensumer.dto.request.EventPostCreateRequest;
import org.swyg.greensumer.dto.request.ReviewCommentCreateRequest;
import org.swyg.greensumer.dto.request.ReviewCommentModifyRequest;
import org.swyg.greensumer.dto.request.ReviewPostModifyRequest;
import org.swyg.greensumer.dto.response.Response;
import org.swyg.greensumer.dto.response.ReviewPostResponse;
import org.swyg.greensumer.dto.response.ReviewPostWithCommentResponse;
import org.swyg.greensumer.service.EventCommentService;
import org.swyg.greensumer.service.EventPostService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventPostService eventPostService;
    private final EventCommentService eventCommentService;

    @PostMapping
    public Response<Void> createEvent(@RequestBody EventPostCreateRequest request, Authentication authentication){
        eventPostService.create(request, request.getStoreId(), request.getProductId(), authentication.getName());

        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<ReviewPostResponse> modifyEvent(@PathVariable Long postId, @RequestBody ReviewPostModifyRequest request, Authentication authentication) throws JsonProcessingException {
        ReviewPost reviewPost = eventPostService.modify(request, postId, request.getProductId(), authentication.getName());


        return Response.success(ReviewPostResponse.fromReviewPost(reviewPost));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> deleteEvent(@PathVariable Long postId, Authentication authentication) {
        eventPostService.delete(postId, authentication.getName());

        return Response.success();
    }

    @GetMapping
    public Response<Page<ReviewPostResponse>> eventList(Pageable pageable, Authentication authentication) {
        return Response.success(eventPostService.list(pageable).map(ReviewPostResponse::fromReviewPost));
    }

    @GetMapping("/{postId}")
    public Response<ReviewPostWithCommentResponse> getEventPostAndComments(@PathVariable Long postId, Authentication authentication) {
        ReviewPostWithComment postWithComment = eventPostService.getPostAndComments(postId, authentication.getName());
        return Response.success(ReviewPostWithCommentResponse.fromReviewPostWithComment(postWithComment));
    }

    @GetMapping("/my")
    public Response<Page<ReviewPostResponse>> myEventList(Pageable pageable, Authentication authentication) throws JsonProcessingException {
        return Response.success(eventPostService.myList(authentication.getName(), pageable).map(ReviewPostResponse::fromReviewPost));
    }

    @PostMapping("/{postId}/comments")
    public Response<Void> createComment(@PathVariable Long postId, @RequestBody ReviewCommentCreateRequest request, Authentication authentication) {
        eventCommentService.createComment(postId, request.getContent(), authentication.getName());

        return Response.success();
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public Response<EventCommentResponse> modifyComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody ReviewCommentModifyRequest request,
            Authentication authentication
    ) {
        EventComment reviewComment = eventCommentService.modifyComment(postId, commentId, request.getContent(), authentication.getName());
        return Response.success(EventCommentResponse.fromEventComment(reviewComment));
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public Response<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, Authentication authentication) {
        eventCommentService.deleteComment(postId, commentId, authentication.getName());
        return Response.success();
    }
}
