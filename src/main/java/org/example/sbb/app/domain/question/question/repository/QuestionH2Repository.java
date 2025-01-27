package org.example.sbb.app.domain.question.question.repository;

import jakarta.persistence.LockModeType;
import org.example.sbb.app.domain.question.question.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface QuestionH2Repository extends JpaRepository<Question, Long> {
    Page<Question> findAll(Pageable pageable);

    Page<Question> findAll(Specification<Question> search, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Question> findById(Long id);
}
