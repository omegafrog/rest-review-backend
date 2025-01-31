package org.example.sbb.app.domain.question.answer.repository;

import org.example.sbb.app.domain.question.answer.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerH2Repository extends JpaRepository<Answer, Long> {
}
