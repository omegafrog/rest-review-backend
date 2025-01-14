package org.example.sbb.app.domain.question;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.sbb.app.domain.dto.QuestionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionH2Repository repository;

    public List<Question> getQuestionList() {
        return repository.findAll();
    }

    public Page<Question> getQuestionPage(Pageable pageable) {
        Pageable newPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.desc("createdAt")));
        return repository.findAll(newPage);
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
