package org.swyg.greensumer.fixture;

import org.swyg.greensumer.domain.ProductEntity;
import org.swyg.greensumer.domain.StoreEntity;

public class ProductEntityFixture {

    public static ProductEntity get(StoreEntity store){
        return ProductEntity.of(
                store,
                "name",
                3000,
                3,
                "description",
                "image"
        );
    }

}
