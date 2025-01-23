package org.example.sbb.jpa;

import org.example.sbb.app.domain.answer.SortOption;
import org.example.sbb.app.domain.dto.QuestionListDto;
import org.example.sbb.app.domain.question.repository.QueryDslQuestionH2Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
public class JpaTest {

    //    @Autowired
//    private QuestionService service;
//    @Test
//    void testJpa() {
//        for (int i = 1; i <= 300; i++) {
//            String subject = String.format("테스트 데이터입니다:[%03d]", i);
//            String content = "내용무";
//            service.writeQuestion(subject, content, null);
//        }
//    }
    @Autowired
    private QueryDslQuestionH2Repository repository;

    @Test
    void getTest(){
        Page<QuestionListDto> all = repository.findAll("", PageRequest.of(0, 5), SortOption.RECENT_ANSWER);
        System.out.println("all = " + all);
        System.out.println(all.getContent());
    }
}
