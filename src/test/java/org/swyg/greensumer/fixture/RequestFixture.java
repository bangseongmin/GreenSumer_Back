package org.swyg.greensumer.fixture;

import org.swyg.greensumer.domain.constant.StoreType;
import org.swyg.greensumer.dto.request.*;

public class RequestFixture {
    private static final Integer productId = 1;
    private static final Integer storeId = 1;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email@email.com";
    private static final String NICKNAME = "nickname";
    private static final String title = "title";
    private static final String BIRTH = "2000-10-01";
    private static final String PHONE = "010-1234-1234";
    private static final boolean GENDER = true;
    private static final String content = "content";
    private static final String image = "image";
    private static final String comment = "comment";
    private static final String NAME = "name";
    private static final String storeType = StoreType.FOOD.toString();
    private static final String description = "description";
    private static final String address = "address";
    private static final String hours = "hours";
    private static final Double lat = 123.123123123;
    private static final Double lng = 123.123123123;
    private static final String logo = "logo";
    private static final String CODE = "code";
    private static final String roadname = "roadname";
    private static final int price = 5000;
    private static final int stock = 5;
    private static final int modifiedPrice = price + 1000;
    private static final int modifiedStock = stock + 1;

    public static UserSignUpRequest UserSignUpRequest() {
        return UserSignUpRequest.of(USERNAME, PASSWORD, NAME, BIRTH, NICKNAME, EMAIL, PHONE, GENDER);
    }

    public static UserLoginRequest UserLoginRequest() {
        return UserLoginRequest.of(USERNAME, PASSWORD);
    }

    public static VerificationRequest VerificationRequest() {
        return VerificationRequest.of(EMAIL);
    }

    public static VerificationCheckRequest VerificationCheckRequest() {
        return VerificationCheckRequest.of(EMAIL, CODE);
    }

    public static UsernameRequest UsernameRequest() {
        return UsernameRequest.of(EMAIL, CODE);
    }

    public static PasswordUpdateRequest PasswordUpdateRequest() {
        return PasswordUpdateRequest.of(USERNAME, EMAIL, CODE, PASSWORD);
    }

    public static ReviewPostCreateRequest getReviewPostCreateRequest() {
        return ReviewPostCreateRequest.of(productId, title, content, image);
    }

    public static ReviewPostModifyRequest getReviewPostModifyRequest() {
        return ReviewPostModifyRequest.of(productId, title, content, image);
    }

    public static ReviewCommentCreateRequest getReviewCommentCreateRequest() {
        return ReviewCommentCreateRequest.of(comment);
    }

    public static ReviewCommentModifyRequest getReviewCommentModifyRequest() {
        return ReviewCommentModifyRequest.of(comment);
    }

    public static StoreCreateRequest getStoreCreateRequest() {
        return StoreCreateRequest.of(NAME, storeType, description, address, roadname, hours, lat, lng, logo);
    }

    public static StoreModifyRequest getStoreModifyRequest() {
        return StoreModifyRequest.of(NAME, storeType, description, address, hours, roadname, lat, lng, logo);
    }

    public static ProductCreateRequest getProductCreateRequest() {
        return ProductCreateRequest.of(NAME, price, stock, description, image);
    }

    public static ProductModifyRequest getProductModifyRequest() {
        return ProductModifyRequest.of(NAME, modifiedPrice, modifiedStock, description, image);
    }


    public static UserLoginRequest getUserLoginRequest() {
        return UserLoginRequest.of(USERNAME, PASSWORD);
    }

    public static VerificationRequest getVerificationRequest() {
        return VerificationRequest.of(EMAIL);
    }

    public static VerificationCheckRequest getVerificationCheckRequest() {
        return VerificationCheckRequest.of(EMAIL, code);
    }

    public static UsernameRequest getUsernameRequest() {
        return UsernameRequest.of(EMAIL, code);
    }

    public static PasswordUpdateRequest getPasswordUpdateRequest() {
        return PasswordUpdateRequest.of(USERNAME, EMAIL, code, PASSWORD);
    }

    public static UpdateUserRequest getUpdateUserRequest(){
        return UpdateUserRequest.of(USERNAME, PASSWORD, EMAIL, NICKNAME, address, lat, lng);
    }

}
