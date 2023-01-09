package org.swyg.greensumer.fixture;

import org.swyg.greensumer.domain.ProductEntity;
import org.swyg.greensumer.domain.ReviewPostEntity;
import org.swyg.greensumer.domain.UserEntity;
import org.swyg.greensumer.domain.constant.UserRole;

public class ReviewPostEntityFixture {

    public static ReviewPostEntity get(String username, Integer postId, Integer userId, Integer productId){
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setUsername(username);
        user.setNickname(username);
        user.setEmail(username);
        user.setRole(UserRole.USER);

        ProductEntity product = new ProductEntity();
        product.setId(productId);

        ReviewPostEntity post = new ReviewPostEntity();
        post.setId(postId);
        post.setUser(user);
        post.setContent(username);
        post.setProduct(product);

        return post;
    }
}
