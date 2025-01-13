package org.example.sbb.app.domain.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionH2Repository repository;

    public List<Question> getQuestionList() {
        return repository.findAll();
    }
}
