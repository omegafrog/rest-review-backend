package org.example.sbb.app.repository;

import org.example.sbb.app.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerH2Repository extends JpaRepository<Answer, Long> {
}
