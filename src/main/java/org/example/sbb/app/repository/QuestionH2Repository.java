package org.example.sbb.app.repository;

import org.example.sbb.app.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionH2Repository extends JpaRepository<Question, Long> {

}
