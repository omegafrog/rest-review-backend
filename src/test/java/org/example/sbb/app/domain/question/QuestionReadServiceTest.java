package org.example.sbb.app.domain.question;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.example.sbb.app.domain.dto.QuestionDto;
import org.example.sbb.app.domain.question.repository.QuestionH2Repository;
import org.example.sbb.app.domain.user.SiteUser;
import org.example.sbb.app.domain.user.UserService;
import org.example.sbb.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class QuestionReadServiceTest {

    @Autowired
    private QuestionH2Repository questionRepository;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp(){
        transactionTemplate.execute(status -> {
            Util.Test.setUp(em);
            return null;
        });
    }

    @Test
    @DisplayName("조회수는 여러 스레드에서 동시에 접근해도 정상적으로 집계되어야 한다.")
    void increaseQuestionViewCountTest() throws InterruptedException {
        Long id = transactionTemplate.execute(status -> {
            SiteUser register = userService.register("user", "password", "password", "email@email.com");
            Question question = new Question("sub", "con", register);
            Question saved = questionRepository.save(question);
            return saved.getId();
        });

        ExecutorService executorService = Executors.newFixedThreadPool(10);

            Runnable task = () -> {
                for (int i = 0; i < 10; i++) {
                    questionService.getQuestionInfo(id, PageRequest.of(0, 5), PageRequest.of(0, 5));
                }
            };

            for (int i = 0; i < 10; i++) {
                executorService.submit(task);
            }
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.MINUTES);


        QuestionDto dto = questionService.getQuestionInfo(id, PageRequest.of(0, 5), PageRequest.of(0, 5));

        Assertions.assertThat(dto.viewCount()).isEqualTo(101);

        }
}