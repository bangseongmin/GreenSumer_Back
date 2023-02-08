package org.swyg.greensumer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.swyg.greensumer.dto.Interview;

import java.util.List;

@AllArgsConstructor
@Getter
public class InterviewResponse {
    List<Interview> interviews;

    public static InterviewResponse of(List<Interview> interviews) {
        return new InterviewResponse(interviews);
    }
}
