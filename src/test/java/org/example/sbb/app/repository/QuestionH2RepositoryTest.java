package org.example.sbb.app.repository;

import org.assertj.core.api.Assertions;
import org.example.sbb.app.entity.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionH2RepositoryTest {
    @Autowired
    private QuestionH2Repository questionH2Repository;

    @BeforeEach
    void setUp(){
        questionH2Repository.deleteAll();
    }
    @Test
    void saveTest(){
        Question q = new Question("hi", "hihi");
        Question save = questionH2Repository.save(q);

        Assertions.assertThat(save).isNotNull();
        Assertions.assertThat(save.getId()).isNotNull();
    }

}