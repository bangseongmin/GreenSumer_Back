package org.swyg.greensumer.fixture;

import org.swyg.greensumer.domain.StoreEntity;
import org.swyg.greensumer.domain.UserEntity;
import org.swyg.greensumer.domain.constant.StoreType;

public class StoreEntityFixture {

    public static StoreEntity get(UserEntity user, String name, String description, String address, String lat, String lng){
        return StoreEntity.of(
                user,
                name,
                description,
                "hours",
                address,
                lat,
                lng,
                "logo",
                StoreType.FOOD
        );
    }
}
