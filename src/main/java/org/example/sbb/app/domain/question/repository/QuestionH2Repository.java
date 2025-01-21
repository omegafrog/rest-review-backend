package org.example.sbb.app.domain.question.repository;

import org.example.sbb.app.domain.question.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionH2Repository extends JpaRepository<Question, Long> {
    Page<Question> findAll(Pageable pageable);

    Page<Question> findAll(Specification<Question> search, Pageable pageable);
}
