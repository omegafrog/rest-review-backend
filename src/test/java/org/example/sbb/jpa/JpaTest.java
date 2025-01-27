package org.example.sbb.jpa;

import org.example.sbb.app.domain.question.question.repository.QueryDslQuestionH2Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JpaTest {

    @Autowired
    private QueryDslQuestionH2Repository repository;

    @Test
    void getTest(){
    }
}
