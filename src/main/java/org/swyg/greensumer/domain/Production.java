package org.swyg.greensumer.domain;

import java.time.LocalDateTime;

public class Production {

    private Long id;

    private Long storeid;

    private String productName;

    private int price;
    private int inventory;

    private String description;
    private String productImage;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}
