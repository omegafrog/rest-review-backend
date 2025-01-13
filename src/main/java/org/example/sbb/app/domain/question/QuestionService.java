package org.example.sbb.app.domain.question;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.sbb.app.domain.dto.QuestionDto;
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

    public void writeQuestion(String subject, String content) {
        Question question = new Question(subject, content);
        repository.save(question);
    }
}
