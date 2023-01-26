package org.swyg.greensumer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.swyg.greensumer.domain.ProductEntity;
import org.swyg.greensumer.domain.ReviewPostEntity;
import org.swyg.greensumer.domain.ReviewPostViewerEntity;
import org.swyg.greensumer.domain.UserEntity;
import org.swyg.greensumer.dto.ReviewPost;
import org.swyg.greensumer.dto.ReviewPostWithComment;
import org.swyg.greensumer.dto.User;
import org.swyg.greensumer.dto.request.ReviewPostCreateRequest;
import org.swyg.greensumer.dto.request.ReviewPostModifyRequest;
import org.swyg.greensumer.exception.ErrorCode;
import org.swyg.greensumer.exception.GreenSumerBackApplicationException;
import org.swyg.greensumer.repository.ReviewPostEntityRepository;
import org.swyg.greensumer.repository.ReviewPostViewerEntityRepository;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ReviewPostService {

    private final ReviewPostEntityRepository reviewPostEntityRepository;
    private final ReviewPostViewerEntityRepository reviewPostViewerEntityRepository;
    private final UserEntityRepositoryService userEntityRepositoryService;
    private final StoreService storeService;
    private final ImageService imageService;

    @Transactional
    public void create(ReviewPostCreateRequest request, String username) {
        UserEntity userEntity = userEntityRepositoryService.findByUsernameOrException(username);
        List<ProductEntity> productEntities = storeService.getProductListOnStore(request.getProducts(), request.getStoreId());

        ReviewPostEntity reviewPostEntity = reviewPostEntityRepository.save(ReviewPostEntity.of(
                userEntity,
                request.getTitle(),
                request.getContent()
        ));

        if(productEntities.size() > 0){
            reviewPostEntity.addProducts(productEntities);
        }

        if(request.getImages().size() > 0){
            reviewPostEntity.addImages(imageService.getImages(request.getImages()));
        }
    }

    @Transactional
    public ReviewPost modify(ReviewPostModifyRequest request, Long postId, String username) {
        ReviewPostEntity reviewPostEntity = getReviewPostEntityOrException(postId);
        isPostMine(reviewPostEntity.getUser().getUsername(), username, postId);
        userEntityRepositoryService.loadUserByUsername(username);

        List<ProductEntity> productEntities = storeService.getProductListOnStore(request.getProducts(), request.getStoreId());

        reviewPostEntity.updateReviewPost(request.getTitle(), request.getContent(), productEntities);

        if(request.getImages().size() > 0){
            reviewPostEntity.addImages(imageService.getImages(request.getImages()));
        }

        return ReviewPost.fromEntity(reviewPostEntity);
    }

    @Transactional
    public void delete(Long postId, String username) {
        ReviewPostEntity reviewPostEntity = getReviewPostEntityOrException(postId);

        userEntityRepositoryService.loadUserByUsername(username);
        isPostMine(reviewPostEntity.getUser().getUsername(), username, postId);

        reviewPostEntityRepository.delete(reviewPostEntity);
    }

    @Transactional(readOnly = true)
    public Page<ReviewPost> list(Pageable pageable) {
        return reviewPostEntityRepository.findAll(pageable).map(ReviewPost::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<ReviewPost> myList(String username, Pageable pageable) {
        User user = userEntityRepositoryService.loadUserByUsername(username);
        return reviewPostEntityRepository.findAllByUser_Id(user.getId(), pageable).map(ReviewPost::fromEntity);
    }

    @Transactional
    public ReviewPostWithComment getPostAndComments(Long postId, String username) {
        UserEntity userEntity = userEntityRepositoryService.findByUsernameOrException(username);

        ReviewPostEntity reviewPostEntity = getReviewPostEntityOrException(postId);

        ReviewPostViewerEntity reviewPostViewerEntity = reviewPostViewerEntityRepository.findByReview_IdAndUser_Id(postId, userEntity.getId()).orElseGet(
                () -> reviewPostViewerEntityRepository.save(ReviewPostViewerEntity.of(reviewPostEntity, userEntity))
        );

        reviewPostEntity.addViewer(reviewPostViewerEntity);

        return ReviewPostWithComment.fromEntity(reviewPostEntity);
    }

    public ReviewPostEntity getReviewPostEntityOrException(Long postId) {
        return reviewPostEntityRepository.findById(postId).orElseThrow(
                () -> {throw new GreenSumerBackApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId));}
        );
    }

    private void isPostMine(String writer, String username, Long postId){
        if(!Objects.equals(writer, username)){
            throw new GreenSumerBackApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", username, postId));
        }
    }

}
