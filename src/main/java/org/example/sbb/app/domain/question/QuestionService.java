package org.example.sbb.app.domain.question;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.sbb.app.QuestionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionH2Repository repository;

    public List<Question> getQuestionList() {
        return repository.findAll();
    }

    public QuestionDto getQuestionInfo(Long id) {
        return QuestionDto.of(repository.findById(id)
                .orElseThrow(EntityNotFoundException::new));
    }
}
