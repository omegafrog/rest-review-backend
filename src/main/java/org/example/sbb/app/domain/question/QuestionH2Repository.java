package org.example.sbb.app.domain.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionH2Repository extends JpaRepository<Question, Long> {
    Page<Question> findAll(Pageable pageable);

}
