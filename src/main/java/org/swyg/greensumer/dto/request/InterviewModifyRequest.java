package org.swyg.greensumer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.swyg.greensumer.domain.constant.UserRole;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewModifyRequest {
    private String writer;
    private String store;
    private String opinion;
    private String target;
}
