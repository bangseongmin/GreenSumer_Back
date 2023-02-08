package org.swyg.greensumer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.swyg.greensumer.domain.InterviewEntity;
import org.swyg.greensumer.domain.constant.UserRole;
import org.swyg.greensumer.dto.Interview;
import org.swyg.greensumer.dto.request.InterviewCreateRequest;
import org.swyg.greensumer.dto.request.InterviewModifyRequest;
import org.swyg.greensumer.exception.ErrorCode;
import org.swyg.greensumer.exception.GreenSumerBackApplicationException;
import org.swyg.greensumer.repository.InterviewEntityRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InterviewService {

    private final InterviewEntityRepository interviewEntityRepository;

    public void saveInterview(InterviewCreateRequest request) {
        interviewEntityRepository.save(InterviewEntity.builder()
                .storeName(request.getStore())
                .writer(request.getWriter())
                .opinion(request.getOpinion())
                .build());
    }

    public void saveInterviews(List<InterviewCreateRequest> requests) {
        List<InterviewEntity> list = new ArrayList<>();
        for (InterviewCreateRequest request : requests) {
            list.add(InterviewEntity.builder()
                    .storeName(request.getStore())
                    .writer(request.getWriter())
                    .opinion(request.getOpinion())
                    .build());
        }

        interviewEntityRepository.saveAll(list);
    }

    public void deleteInterview(Long interviewId) {
        interviewEntityRepository.deleteById(interviewId);
    }

    @Transactional
    public void modifyInterview(InterviewModifyRequest request) {
        InterviewEntity interviewEntity = interviewEntityRepository.findById(Long.valueOf(request.getInterviewId())).orElseThrow(() -> {
            throw new GreenSumerBackApplicationException(ErrorCode.INTERVIEW_NOT_FOUND, String.format("interview-%d not founded", request.getInterviewId()));
        });

        interviewEntity.updateInterview(request.getWriter(), request.getStore(), request.getOpinion(), request.getTarget());
    }


    public Page<Interview> getInterviewsFromUser(Pageable pageable) {
        return interviewEntityRepository.findAllByTarget(UserRole.USER, pageable).map(Interview::fromInterviewEntity);
    }

    public Page<Interview> getInterviewFromSeller(Pageable pageable) {
        return interviewEntityRepository.findAllByTarget(UserRole.SELLER, pageable).map(Interview::fromInterviewEntity);
    }
}
