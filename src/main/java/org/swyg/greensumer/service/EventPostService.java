package org.swyg.greensumer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.swyg.greensumer.domain.*;
import org.swyg.greensumer.dto.EventPost;
import org.swyg.greensumer.dto.ReviewPost;
import org.swyg.greensumer.dto.request.EventPostCreateRequest;
import org.swyg.greensumer.dto.request.ReviewPostCreateRequest;
import org.swyg.greensumer.dto.request.ReviewPostModifyRequest;
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

    public void create(EventPostCreateRequest request, Long storeId, Long productId, String username) {
        UserEntity userEntity = userEntityRepositoryService.findByUsernameOrException(username);
        StoreEntity storeEntity = storeService.getStoreEntityOrException(storeId);
        ProductEntity productEntity = storeService.getProductEntityOrException(productId);

        EventPostEntity eventPostEntity
                = eventPostEntityRepository.save(EventPostEntity.builder()
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
    public EventPost modify(EventPostCreateRequest request, Long postId, Long productId, String username) {
        EventPostEntity eventPostEntity = eventPostEntityRepository.findById(postId).orElseThrow(() -> {
            throw new GreenSumerBackApplicationException(ErrorCode.EVENT_NOT_FOUND, String.format("Event %s not founded", postId));
        });

        isEventMine(eventPostEntity.getUser().getUsername(), username, postId);

        userEntityRepositoryService.loadUserByUsername(username);

        ProductEntity productEntity = storeService.getProductEntityOrException(productId);

        eventPostEntity.updateEvent(productEntity, request.getTitle(), request.getContent());

        if(request.getImages().size() > 0){
            eventPostEntity.addImages(imageService.findAllByIdIn(request.getImages()));
        }

        return EventPost.fromEntity(eventPostEntity);
    }




    private void isEventMine(String writer, String username, Long postId){
        if(!Objects.equals(writer, username)){
            throw new GreenSumerBackApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", username, postId));
        }
    }
}
