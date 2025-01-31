package org.example.sbb.app.repository;

import org.assertj.core.api.Assertions;
import org.example.sbb.app.domain.comment.Comment;
import org.example.sbb.app.domain.comment.repository.CommentH2Repository;
import org.example.sbb.app.domain.question.answer.Answer;
import org.example.sbb.app.domain.question.answer.repository.AnswerH2Repository;
import org.example.sbb.app.domain.question.question.Question;
import org.example.sbb.app.domain.question.question.repository.QuestionH2Repository;
import org.example.sbb.app.domain.user.SiteUser;
import org.example.sbb.app.domain.user.UserH2Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerH2RepositoryTest {

    @Autowired
    private AnswerH2Repository answerH2Repository;

    @Autowired
    private UserH2Repository userH2Repository;

    @Autowired
    private QuestionH2Repository questionH2Repository;
    @Autowired
    private CommentH2Repository commentH2Repository;

    @Test
    void saveNFindTest(){

        SiteUser saved = userH2Repository.save(new SiteUser("user", "password", "email@Email.com"));

        Question savedQuestion = questionH2Repository.save(new Question("question", "question description", saved));

        Answer answer = Answer.builder()
                .content("con1")
                .author(saved)
                .question(savedQuestion)
                .build();

        Answer savedAnswer = answerH2Repository.save(answer);

        Assertions.assertThat(answerH2Repository.findById(savedAnswer.getId())).isNotEmpty();
        Assertions.assertThat(answerH2Repository.findById(savedAnswer.getId())).get().isEqualTo(savedAnswer);
    }

    @Test
    void deleteTest(){
        // given
        SiteUser saved = userH2Repository.save(new SiteUser("user", "password", "email@Email.com"));

        Question savedQuestion = questionH2Repository.save(new Question("question", "question description", saved));

        Answer answer = Answer.builder()
                .content("con1")
                .author(saved)
                .question(savedQuestion)
                .build();

        Answer savedAnswer = answerH2Repository.save(answer);

        Comment savedComment = commentH2Repository.save(Comment.builder()
                .targetAnswer(savedAnswer)
                .author(saved)
                .content("hi")
                .build());

        //when
        answerH2Repository.deleteById(savedAnswer.getId());

        //then
        Assertions.assertThat(answerH2Repository.findById(savedAnswer.getId())).isEmpty();
        Assertions.assertThat(commentH2Repository.findById(savedComment.getId())).isEmpty();
    }
}