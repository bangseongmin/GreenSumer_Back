package org.swyg.greensumer.fixture;

import org.swyg.greensumer.domain.constant.UserRole;
import org.swyg.greensumer.dto.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

public class Fixtures {

    public static ReviewPost getReviewPost() {
        final ReviewPost reviewPost = new ReviewPost(1, "title", "content", "hashtag", "imagePath", getProduct(), getUser(), getNow(), null, null);
        return reviewPost;
    }

    public static Product getProduct() {
        final Product product = new Product(1, "name", 2000, 3, "description", "image", getNow(), null, null);
        return product;
    }

    public static User getUser() {
        final User user = new User(1, "username", "password", "email", "nickname", "address", "lat", "lng", UserRole.USER, getNow(), null, null);
        return user;
    }

    public static Store getStore() {
        final Store store = new Store(1, getUser(), "name", "description", "address", "hours", "lat", "lng", "logo", getNow(), null, null);
        return store;
    }

    private static Timestamp getNow() {
        return Timestamp.from(Instant.now());
    }

    public static ReviewComment getReviewComment() {
        final ReviewComment reviewComment = new ReviewComment(1, "content", getUser(), getReviewPost(), getNow(), null, null);
        return reviewComment;
    }

    public static ReviewPostWithComment getReviewPostWithComment() {
        final ReviewPostWithComment reviewPostWithComment = new ReviewPostWithComment(1, "title", "content", "hasttag", "imagepath", getProduct(), getUser(), Set.of(getReviewComment()), getNow(), null, null);
        return reviewPostWithComment;
    }

}
