package org.example.sbb.app.repository;

import org.assertj.core.api.Assertions;
import org.example.sbb.app.entity.Answer;
import org.example.sbb.app.entity.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnswerH2RepositoryTest {

    @BeforeEach
    void setUp() {
        answerH2Repository.deleteAll();
        questionH2Repository.deleteAll();
    }
    @Autowired
    private AnswerH2Repository answerH2Repository;
    @Autowired
    private QuestionH2Repository questionH2Repository;

    @Test
    void saveTest(){
        Question question = new Question("hi", "hihi");
        Question saved = questionH2Repository.save(question);
        Answer answer = new Answer(saved, "bye" );
        Answer savedAnswer = answerH2Repository.save(answer);

        Assertions.assertThat(savedAnswer).isNotNull();
        Assertions.assertThat(savedAnswer.getId()).isNotNull();
    }

}