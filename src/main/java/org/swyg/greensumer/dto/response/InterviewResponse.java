package org.swyg.greensumer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.swyg.greensumer.dto.Interview;
@AllArgsConstructor
@Getter
public class InterviewResponse {
    Page<Interview> seller;
    Page<Interview> customer;

    public static InterviewResponse of(Page<Interview> seller, Page<Interview> customer) {
        return new InterviewResponse(seller, customer);
    }
}
