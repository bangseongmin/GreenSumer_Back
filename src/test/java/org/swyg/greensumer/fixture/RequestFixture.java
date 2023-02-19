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
    private static final String AccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRlc3QxIiwiaWF0IjoxNjc2NzkwNDgwLCJleHAiOjE2NzY3OTIyODB9.NhUIC2NSpLU1dBpoyQWjGU9O-nNfMczerYl-naPrihk";
    private static final String RefreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NzY3OTA0ODAsImV4cCI6MTY3NzM5NTI4MH0.Bk5sjSRHUaM-H0A1y_OloAwvX0G0HcJOLRND_H6yws0";

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

    public static PasswordUpdateRequest PasswordUpdateRequest() {
        return PasswordUpdateRequest.of(USERNAME, EMAIL, CODE, PASSWORD);
    }

    public static UserLogoutRequest UserLogoutRequest() {
        return UserLogoutRequest.of(AccessToken, RefreshToken);
    }

    public static UserReissueRequest UserReissueRequest() {
        return UserReissueRequest.of(AccessToken, RefreshToken);
    }

}
