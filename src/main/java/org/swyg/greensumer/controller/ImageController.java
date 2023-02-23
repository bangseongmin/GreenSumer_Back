package org.swyg.greensumer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.swyg.greensumer.dto.Image;
import org.swyg.greensumer.dto.request.ImageCreateRequest;
import org.swyg.greensumer.dto.request.ImageModifyRequest;
import org.swyg.greensumer.dto.request.ImagesCreateRequest;
import org.swyg.greensumer.dto.response.ImageCreateResponse;
import org.swyg.greensumer.dto.response.Response;
import org.swyg.greensumer.service.ImageService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public Response<Void> saveImages(@ModelAttribute ImagesCreateRequest request, Authentication authentication) throws IOException {
        imageService.saveImages(request, request.getType(), authentication.getName());

        return Response.success();
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<?> searchImage(@PathVariable Long imageId, @RequestParam(value = "type") String type) {
        byte[] image = imageService.searchImage(imageId, type);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(image);
    }

    @PutMapping( "/{imageId}")
    public Response<Void> modifyImage(@PathVariable Long imageId, @ModelAttribute ImageModifyRequest request, Authentication authentication) throws IOException {
        imageService.modifyImage(imageId, request, authentication.getName());
        return Response.success();
    }

    @DeleteMapping("/{imageId}")
    public Response<Void> removeImage(@PathVariable Long imageId, @RequestParam(value = "type") String type, Authentication authentication) {
        imageService.removeImage(imageId, type, authentication.getName());
        return Response.success();
    }

}
