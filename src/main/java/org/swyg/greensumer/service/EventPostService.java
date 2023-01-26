package org.swyg.greensumer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.swyg.greensumer.domain.*;
import org.swyg.greensumer.dto.EventPost;
import org.swyg.greensumer.dto.EventPostWithComment;
import org.swyg.greensumer.dto.User;
import org.swyg.greensumer.dto.request.EventPostCreateRequest;
import org.swyg.greensumer.dto.request.EventPostModifyRequest;
import org.swyg.greensumer.exception.ErrorCode;
import org.swyg.greensumer.exception.GreenSumerBackApplicationException;
import org.swyg.greensumer.repository.EventPostEntityRepository;
import org.swyg.greensumer.repository.EventPostViewerEntityRepository;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class EventPostService {

    private final EventPostEntityRepository eventPostEntityRepository;
    private final EventPostViewerEntityRepository eventPostViewerEntityRepository;

    private final UserEntityRepositoryService userEntityRepositoryService;
    private final StoreService storeService;
    private final ImageService imageService;

    @Transactional
    public void create(EventPostCreateRequest request, Long storeId, Long productId, String username) {
        UserEntity userEntity = userEntityRepositoryService.findByUsernameOrException(username);
        StoreEntity storeEntity = storeService.getStoreEntityOrException(storeId);
        ProductEntity productEntity = storeService.getProductEntityOrException(productId);

        EventPostEntity eventPostEntity = eventPostEntityRepository.save(EventPostEntity.builder()
                .product(productEntity)
                .user(userEntity)
                .title(request.getTitle())
                .content(request.getContent())
                .build());

        if (request.getImages().size() > 0) {
            eventPostEntity.addImages(imageService.findAllByIdIn(request.getImages()));
        }
    }

    @Transactional
    public EventPost modify(EventPostModifyRequest request, Long postId, Long productId, String username) {
        EventPostEntity eventPostEntity = getEventPostEntityOrException(postId);

        isEventMine(eventPostEntity.getUser().getUsername(), username, postId);
        userEntityRepositoryService.loadUserByUsername(username);
        ProductEntity productEntity = storeService.getProductEntityOrException(productId);

        eventPostEntity.updateEventPost(productEntity, request.getTitle(), request.getContent());

        if(request.getImages().size() > 0){
            eventPostEntity.addImages(imageService.findAllByIdIn(request.getImages()));
        }

        return EventPost.fromEntity(eventPostEntity);
    }

    @Transactional
    public void delete(Long postId, String username) {
        EventPostEntity eventPostEntity = getEventPostEntityOrException(postId);
        userEntityRepositoryService.loadUserByUsername(username);
        isEventMine(eventPostEntity.getUser().getUsername(), username, postId);

        eventPostEntityRepository.delete(eventPostEntity);
    }

    @Transactional(readOnly = true)
    public Page<EventPost> list(Pageable pageable) {
        return eventPostEntityRepository.findAll(pageable).map(EventPost::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<EventPost> myList(String username, Pageable pageable) {
        User user = userEntityRepositoryService.loadUserByUsername(username);
        return eventPostEntityRepository.findAllByUser_Id(user.getId(), pageable).map(EventPost::fromEntity);
    }

    @Transactional
    public EventPostWithComment getPostAndComments(Long postId, String username) {
        UserEntity userEntity = userEntityRepositoryService.findByUsernameOrException(username);

        EventPostEntity eventPostEntity = getEventPostEntityOrException(postId);

        EventPostViewerEntity eventPostViewerEntity = eventPostViewerEntityRepository.findByEvent_IdAndUser_Id(postId, userEntity.getId()).orElseGet(
                () -> eventPostViewerEntityRepository.save(EventPostViewerEntity.of(eventPostEntity, userEntity))
        );

        eventPostEntity.addViewer(eventPostViewerEntity);

        return EventPostWithComment.fromEntity(eventPostEntity);
    }

    public EventPostEntity getEventPostEntityOrException(Long postId) {
        return eventPostEntityRepository.findById(postId).orElseThrow(() -> {
            throw new GreenSumerBackApplicationException(ErrorCode.EVENT_NOT_FOUND, String.format("Event %s not founded", postId));
        });
    }

    private void isEventMine(String writer, String username, Long postId){
        if(!Objects.equals(writer, username)){
            throw new GreenSumerBackApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", username, postId));
        }
    }
}