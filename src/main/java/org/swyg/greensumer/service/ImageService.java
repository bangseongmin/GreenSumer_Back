package org.swyg.greensumer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.swyg.greensumer.domain.ImageEntity;
import org.swyg.greensumer.domain.UserEntity;
import org.swyg.greensumer.domain.constant.ImageType;
import org.swyg.greensumer.dto.Image;
import org.swyg.greensumer.dto.request.ImageModifyRequest;
import org.swyg.greensumer.dto.request.ImagesCreateRequest;
import org.swyg.greensumer.exception.ErrorCode;
import org.swyg.greensumer.exception.GreenSumerBackApplicationException;
import org.swyg.greensumer.repository.ImageEntityRepository;
import org.swyg.greensumer.utils.ImageUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ImageService {
    public final static int IMAGE_UPLOAD_MAX_COUNT = 5;
    private final static long IMAGE_UPLOAD_MAX_SIZE = 10_000_001L;
    private final ImageEntityRepository imageEntityRepository;
    private final UserEntityRepositoryService userEntityRepositoryService;

    @Transactional
    public Image saveImage(MultipartFile image, String type, String username) throws IOException {
        UserEntity userEntity = userEntityRepositoryService.findByUsernameOrException(username);

        if (Objects.isNull(image)) {
            throw new GreenSumerBackApplicationException(ErrorCode.IMAGE_IS_NULL, "Image is Null");
        }

        String originFilename = image.getOriginalFilename();
        String savedFilename = getSavedFilename(originFilename);

        validateImageSize(image.getSize(), originFilename);

        ImageEntity imageEntity = imageEntityRepository.save(
                ImageEntity.of(ImageType.valueOf(type), userEntity, originFilename, savedFilename, ImageUtils.compressImage(image.getBytes()))
        );

        return Image.fromEntity(imageEntity);
    }

    public Image searchImage(Long imageId, String username) {
        userEntityRepositoryService.loadUserByUsername(username);

        ImageEntity imageEntity = getImageEntityOrException(imageId);
        imageEntity.setImageData(ImageUtils.decompressImage(imageEntity.getImageData()));

        return Image.fromEntity(imageEntity);
    }

    @Transactional
    public List<Image> saveImages(ImagesCreateRequest request, String username) throws IOException {
        if (request.getImages().size() > IMAGE_UPLOAD_MAX_COUNT) {
            throw new GreenSumerBackApplicationException(ErrorCode.OVER_IMAGE_COUNT, String.format("Max Image count is 5, but requesting size is %s", request.getImages().size()));
        }

        UserEntity userEntity = userEntityRepositoryService.findByUsernameOrException(username);

        List<ImageEntity> imageEntities = new ArrayList<>();
        ImageType type = ImageType.valueOf(request.getType());

        for (MultipartFile image : request.getImages()) {
            validateImageSize(image.getSize(), image.getOriginalFilename());

            String savedFilename = getSavedFilename(image.getOriginalFilename());
            ImageEntity entity = ImageEntity.of(type, userEntity, image.getOriginalFilename(), savedFilename, ImageUtils.compressImage(image.getBytes()));
            entity.setProduct(null);
            entity.setStore(null);

            imageEntities.add(entity);
        }

        return imageEntityRepository.saveAll(imageEntities).stream().map(Image::fromEntity).collect(Collectors.toList());
    }

    private void validateImageSize(long size, String filename) {
        if (size >= IMAGE_UPLOAD_MAX_SIZE) {
            throw new GreenSumerBackApplicationException(ErrorCode.IMAGE_SIZE_OVER, String.format("%s Image size exceeds 10 MB. Check the Image Size", filename));
        }
    }

    public Image modifyImage(Long imageId, ImageModifyRequest request, String username) throws IOException {
        userEntityRepositoryService.loadUserByUsername(username);

        ImageEntity imageEntity = getImageEntityOrException(imageId);

        validateImageSize(request.getImage().getSize(), imageEntity.getOriginFilename());

        imageEntity.setImageData(ImageUtils.compressImage(request.getImage().getBytes()));
        imageEntity.setImageType(ImageType.valueOf(request.getType()));

        return Image.fromEntity(imageEntity);
    }

    public void removeImage(Long imageId, String username) {
        userEntityRepositoryService.loadUserByUsername(username);

        imageEntityRepository.deleteById(imageId);
    }

    private static String getSavedFilename(String filename) {
        return UUID.randomUUID() + "_" + filename;
    }

    private ImageEntity getImageEntityOrException(Long imageId) {
        return imageEntityRepository.findById(imageId).orElseThrow(() -> {
            throw new GreenSumerBackApplicationException(ErrorCode.IMAGE_NOT_FOUND, String.format("%s not founded", imageId));
        });
    }

    public List<ImageEntity> getImages(List<Long> imageIds) {
        return imageEntityRepository.findAllByIdIn(imageIds);
    }

}
