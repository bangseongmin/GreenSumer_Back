package org.swyg.greensumer.fixture;

import org.swyg.greensumer.domain.constant.StoreType;
import org.swyg.greensumer.dto.request.*;

public class RequestFixture {
    private static final Integer productId = 1;
    private static final String username = "username";
    private static final String password = "password";
    private static final String email = "email";
    private static final String nickname = "nickname";
    private static final String title = "title";
    private static final String content = "content";
    private static final String hashtag = "hashtag";
    private static final String image = "image";
    private static final String comment = "comment";
    private static final String name = "name";
    private static final String storeType = StoreType.FOOD.toString();
    private static final String description = "description";
    private static final String address = "address";
    private static final String hours = "hours";
    private static final String lat = "lat";
    private static final String lng = "lng";
    private static final String logo = "logo";
    private static final int price = 5000;
    private static final int stock = 5;

    public static ReviewPostCreateRequest getReviewPostCreateRequest() {
        return ReviewPostCreateRequest.of(productId, title, content, hashtag, image);
    }

    public static ReviewPostModifyRequest getReviewPostModifyRequest() {
        return ReviewPostModifyRequest.of(productId, title, content, hashtag, image);
    }

    public static ReviewCommentCreateRequest getReviewCommentCreateRequest() {
        return ReviewCommentCreateRequest.of(comment);
    }

    public static ReviewCommentModifyRequest getReviewCommentModifyRequest() {
        return ReviewCommentModifyRequest.of(comment);
    }

    public static StoreCreateRequest getStoreCreateRequest() {
        return StoreCreateRequest.of(name, storeType, description, address, hours, lat, lng, logo);
    }

    public static StoreModifyRequest getStoreModifyRequest() {
        return StoreModifyRequest.of(name, storeType, description, address, hours, lat, lng, logo);
    }

    public static ProductCreateRequest getProductCreateRequest() {
        return ProductCreateRequest.of(name, price, stock, description, image);
    }

    public static ProductModifyRequest getProductModifyRequest() {
        return ProductModifyRequest.of(name, price, stock, description, image);
    }

    public static UserSignUpRequest getUserSignUpRequest() {
        return UserSignUpRequest.of(username, password, email, nickname, address, lat, lng);
    }

    public static UserLoginRequest getUserLoginRequest() {
        return UserLoginRequest.of(username, password);
    }

}
