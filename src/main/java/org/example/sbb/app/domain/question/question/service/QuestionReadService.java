package org.example.sbb.app.domain.question.question.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.sbb.app.domain.question.question.Question;
import org.example.sbb.app.domain.question.question.repository.QuestionH2Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionReadService {

    private final QuestionH2Repository repository;

    public Question getQuestion(Long questionId) {
        return repository.findById(questionId).orElseThrow(EntityNotFoundException::new);
    }
}
