package org.example.sbb.app.domain.question.question.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.example.sbb.app.domain.question.answer.Answer;
import org.example.sbb.app.domain.question.answer.SortOption;
import org.example.sbb.app.domain.question.answer.repository.AnswerH2Repository;
import org.example.sbb.app.domain.question.question.Question;
import org.example.sbb.app.domain.user.SiteUser;
import org.example.sbb.app.domain.user.UserH2Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
class QueryDslQuestionH2RepositoryTest {

    @Autowired
    private QueryDslQuestionH2Repository queryDslQuestionH2Repository;
    @Autowired
    private QuestionH2Repository questionH2Repository;
    @Autowired
    private UserH2Repository userH2Repository;
    @Autowired
    private AnswerH2Repository answerH2Repository;

    @PersistenceContext
    private EntityManager em;
    @BeforeEach
    void setUp(){
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        em.createNativeQuery("TRUNCATE TABLE QUESTION").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE ANSWER").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE QUESTION_VOTER").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE ANSWER_VOTER").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE MEMBER").executeUpdate();
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    @Test
    @Transactional
    void findByIdTest(){
        SiteUser savedUser1 = userH2Repository.save(new SiteUser("user", "password", "email"));
        Question saved1 = questionH2Repository.save(new Question("sub1", "cont1", savedUser1));

        for (int i = 0 ;i < 10; ++i){
            answerH2Repository.save(new Answer(saved1, "content1", savedUser1));
        }

        Question byId = queryDslQuestionH2Repository.findById(saved1.getId(), PageRequest.of(1, 5), SortOption.TIME);
        Assertions.assertThat(byId.getAnswers().size()).isEqualTo(10);
    }


}