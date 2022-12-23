package org.swyg.greensumer.domain;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

public class Store {

    private Long id;
    private Set<UserAccount> userAccounts = new LinkedHashSet<>();

    private String storeName;
    private String storeType;
    private String description;
    private String address;
    private String operationTime;
    private String storeImage;
    private String location;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}
