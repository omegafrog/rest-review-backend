package org.example.sbb.app.repository;

import org.assertj.core.api.Assertions;
import org.example.sbb.app.domain.question.Question;
import org.example.sbb.app.domain.question.repository.QuestionH2Repository;
import org.example.sbb.app.domain.user.SiteUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
        Question q = new Question("hi", "hihi", null);
        Question save = questionH2Repository.save(q);

        Assertions.assertThat(save).isNotNull();
        Assertions.assertThat(save.getId()).isNotNull();
    }

    @Test
    void increaseViewCountTest2(){

        Question q = new Question("hi", "hihi", new SiteUser());
        Question save = questionH2Repository.save(q);
        save.increaseViewCount();
        questionH2Repository.save(save);


        Question question = questionH2Repository.findById(save.getId()).get();
        Assertions.assertThat(question.getViewCount()).isEqualTo(1);
    }


}