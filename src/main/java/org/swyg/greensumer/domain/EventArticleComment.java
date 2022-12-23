package org.swyg.greensumer.domain;

import java.time.LocalDateTime;

public class EventArticleComment {
    private Long id;

    private Long userid;
    private Long eventid;

    private String title;
    private String content;

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
}
