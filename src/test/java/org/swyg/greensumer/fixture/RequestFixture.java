package org.swyg.greensumer.fixture;

import org.swyg.greensumer.domain.constant.StoreType;
import org.swyg.greensumer.dto.request.*;

public class RequestFixture {

    public static ReviewPostCreateRequest getReviewPostCreateRequest(){
        final Integer productId = 1;
        final String title = "new title";
        final String content = "new content";
        final String hashtag = "new hashtag";
        final String image = "new image";
        return ReviewPostCreateRequest.of(productId, title, content, hashtag, image);
    }

    public static ReviewPostModifyRequest getReviewPostModifyRequest(){
        final Integer productId = 1;
        final String title = "modified title";
        final String content = "modified content";
        final String hashtag = "modified hashtag";
        final String image = "modified image";
        return ReviewPostModifyRequest.of(productId, title, content, hashtag, image);
    }

    public static ReviewCommentCreateRequest getReviewCommentCreateRequest(){
        String comment = "new comment";
        return ReviewCommentCreateRequest.of(comment);
    }

    public static ReviewCommentModifyRequest getReviewCommentModifyRequest(){
        String comment = "modify comment";
        return ReviewCommentModifyRequest.of(comment);
    }

    public static StoreCreateRequest getStoreCreateRequest(){
        String name = "name";
        String type = StoreType.FOOD.toString();
        String description = "description";
        String address = "address";
        String hours = "hours";
        String lat = "lat";
        String lng = "lng";
        String logo = "logo";
        return StoreCreateRequest.of(name, type, description, address, hours, lat, lng, logo);
    }

    public static StoreModifyRequest getStoreModifyRequest(){
        String name = "name";
        String type = StoreType.FOOD.toString();
        String description = "description";
        String address = "address";
        String hours = "hours";
        String lat = "lat";
        String lng = "lng";
        String logo = "logo";
        return StoreModifyRequest.of(name, type, description, address, hours, lat, lng, logo);
    }
}
