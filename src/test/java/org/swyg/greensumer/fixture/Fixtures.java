package org.swyg.greensumer.fixture;

import org.swyg.greensumer.domain.*;
import org.swyg.greensumer.domain.constant.StoreType;
import org.swyg.greensumer.domain.constant.UserRole;
import org.swyg.greensumer.dto.Store;
import org.swyg.greensumer.dto.StoreProduct;
import org.swyg.greensumer.dto.TokenInfo;
import org.swyg.greensumer.dto.User;

import java.util.Set;

public class Fixtures {
    private static final String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImFkbWluIiwiaWF0IjoxNjczMDIwMDYwLCJleHAiOjE2NzU2MTIwNjB9.USuRnODheSfeL65rpXqQOMkVLnOqCtSacrJsLNQXSNg";
    private static final long id = 1L;
    private static final int price = 5000;
    private static final int stock = 5;
    private static final int modifiedPrice = price + 1000;
    private static final int modifiedStock = stock + 1;
    private static final String title = "title";
    private static final String content = "content";
    private static final String image = "image";
    private static final String username = "username";
    private static final String password = "password";
    private static final String email = "email";
    private static final String nickname = "nickname";
    private static final String phone = "010-1234-1234";
    private static final String name = "name";
    private static final String modifiedName = "modified Name";
    private static final String address = "address";
    private static final String description = "description";
    private static final Double lat = 123.123123123123;
    private static final Double lng = 123.123123123123;
    private static final String hours = "hours";
    private static final String logo = "logo";
    private static final String roadname = "roadname";
    private static final String url = "http://www.naver.cion";
    private static final String AccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRlc3QxIiwiaWF0IjoxNjc2NzkwNDgwLCJleHAiOjE2NzY3OTIyODB9.NhUIC2NSpLU1dBpoyQWjGU9O-nNfMczerYl-naPrihk";
    private static final String RefreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NzY3OTA0ODAsImV4cCI6MTY3NzM5NTI4MH0.Bk5sjSRHUaM-H0A1y_OloAwvX0G0HcJOLRND_H6yws0";

    public static UserEntity userEntity(){
        return UserEntity.builder()
                .username(username)
                .password(password)
                .email(email)
                .fullname(name)
                .nickname(nickname)
                .phone(phone)
                .gender(true)
                .roles(getRoles())
                .build();
    }

    private static Set<UserRole> getRoles() {
        return Set.of(UserRole.ROLE_USER, UserRole.ROLE_SELLER);
    }

    public static User getUser() {
        return User.fromEntity(userEntity());
    }

    public static TokenInfo getTokenInfo() {
        return TokenInfo.builder()
                .accessToken(AccessToken)
                .refreshToken(RefreshToken)
                .build();
    }

    public static StoreEntity StoreEntity() {
        return StoreEntity.builder()
                .id(id)
                .name(name)
                .phone(phone)
                .storeType(StoreType.FOOD)
                .description(description)
                .url(url)
                .hours(hours)
                .address(addressEntity())
                .logos(Set.of(StoreImageEntity()))
                .build();
    }

    private static StoreImageEntity StoreImageEntity() {
        return StoreImageEntity.builder()
                .originFilename("original")
                .savedFilename("savedFile")
                .userEntity(userEntity())
                .imageData("imageData".getBytes())
                .build();
    }

    private static ProductImageEntity ProductImageEntity() {
        return ProductImageEntity.builder()
                .originFilename("original")
                .savedFilename("savedFile")
                .userEntity(userEntity())
                .imageData("imageData".getBytes())
                .build();
    }

    public static Store Store() {
        return Store.fromEntity(StoreEntity());
    }

    public static AddressEntity addressEntity() {
        return AddressEntity.builder()
                .id(id)
                .address(address)
                .roadname(roadname)
                .latitude(lat)
                .longitude(lng)
                .build();
    }

    public static ProductEntity ProductEntity() {
        return ProductEntity.builder()
                .id(1L)
                .name(name)
                .price(price)
                .description(description)
                .images(Set.of(ProductImageEntity()))
                .stock(1)
                .build();
    }

    public static StoreProductEntity StoreProductEntity() {
        return StoreProductEntity.builder()
                .store(StoreEntity())
                .product(ProductEntity())
                .build();
    }

    public static StoreProduct StoreProduct() {
        return StoreProduct.fromEntity(StoreProductEntity());
    }

}
