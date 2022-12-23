package org.swyg.greensumer.domain;

import java.time.LocalDateTime;

public class EventArticle {

    private Long id;

    private Long userid;
    private Long storeid;

    private String title;
    private String content;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}
