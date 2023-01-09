package org.swyg.greensumer.fixture;

import org.swyg.greensumer.domain.ProductEntity;
import org.swyg.greensumer.domain.StoreEntity;
import org.swyg.greensumer.domain.UserEntity;
import org.swyg.greensumer.domain.constant.StoreType;
import org.swyg.greensumer.domain.constant.UserRole;
import org.swyg.greensumer.dto.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

public class Fixtures {
    private static final String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImFkbWluIiwiaWF0IjoxNjczMDIwMDYwLCJleHAiOjE2NzU2MTIwNjB9.USuRnODheSfeL65rpXqQOMkVLnOqCtSacrJsLNQXSNg";
    private static final Integer id = 1;
    private static final int price = 5000;
    private static final int stock = 5;
    private static final int modifiedPrice = price + 1000;
    private static final int modifiedStock = stock + 1;
    private static final String title = "title";
    private static final String content = "content";
    private static final String hashtag = "hashtag";
    private static final String image = "image";
    private static final String username = "username";
    private static final String password = "password";
    private static final String email = "email";
    private static final String nickname = "nickname";
    private static final String name = "name";
    private static final String modifiedName = "modified Name";
    private static final String address = "address";
    private static final String description = "description";
    private static final String lat = "lat";
    private static final String lng = "lng";
    private static final String hours = "hours";
    private static final String logo = "logo";
    private static final UserRole userRole = UserRole.USER;
    private static final UserRole sellerRole = UserRole.SELLER;

    public static ReviewPost getReviewPost() {
        return new ReviewPost(id, title, content, hashtag, image, getProduct(), getUser(), getNow(), null, null);
    }

    public static Product getProduct() {
        return new Product(id, name, price, stock, description, image, getNow(), null, null);
    }

    public static User getUser() {
        return new User(id, username, password, email, nickname, address, lat, lng, userRole, getNow(), null, null);
    }

    public static Store getStore() {
        return new Store(id, getUser(), name, description, address, hours, lat, lng, logo, getNow(), null, null);
    }

    private static Timestamp getNow() {
        return Timestamp.from(Instant.now());
    }

    public static ReviewComment getReviewComment() {
        return new ReviewComment(id, content, getUser(), getReviewPost(), getNow(), null, null);
    }

    public static ReviewPostWithComment getReviewPostWithComment() {
        return new ReviewPostWithComment(id, title, content, hashtag, image, getProduct(), getUser(), Set.of(getReviewComment()), getNow(), null, null);
    }

    public static String getToken() {
        return token;
    }
    public static String getUsername() { return username; }
    public static String getPassword() { return password; }
    public static String getName() { return name; }
    public static Integer getId() { return id; }

    public static UserEntity createUserEntity() {
        return UserEntity.of(username, password, nickname, email, address, lat, lng);
    }

    public static StoreEntity createStoreEntity() {
        return StoreEntity.of(id, createUserEntity(), name, description, address, hours, lat, lng, logo, StoreType.FOOD);
    }

    public static ProductEntity createProductEntity() {
        return ProductEntity.of(id, createStoreEntity(), name, price, stock, description, image);
    }

    public static ProductEntity ModifyProductEntity(ProductEntity entity) {
        entity.setName(modifiedName);
        entity.setStock(modifiedStock);
        entity.setPrice(modifiedPrice);
        return entity;
    }

    public static UserEntity createSignUpUserAccount() {
        UserEntity userEntity = createUserEntity();
        userEntity.setId(id);
        return userEntity;
    }


}
