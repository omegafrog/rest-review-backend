package org.example.sbb.app.repository;

import org.assertj.core.api.Assertions;
import org.example.sbb.app.domain.comment.Comment;
import org.example.sbb.app.domain.comment.CommentH2Repository;
import org.example.sbb.app.domain.comment.dto.CommentForm;
import org.example.sbb.app.domain.question.answer.Answer;
import org.example.sbb.app.domain.question.answer.repository.AnswerH2Repository;
import org.example.sbb.app.domain.question.question.Question;
import org.example.sbb.app.domain.question.question.repository.QuestionH2Repository;
import org.example.sbb.app.domain.user.SiteUser;
import org.example.sbb.app.domain.user.UserH2Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionH2RepositoryTest {
    @Autowired
    private QuestionH2Repository questionH2Repository;
    @Autowired
    private UserH2Repository userH2Repository;
    @Autowired
    private AnswerH2Repository answerH2Repository;
    @Autowired
    private CommentH2Repository commentH2Repository;

    @BeforeEach
    void setUp(){
        questionH2Repository.deleteAll();
    }
    @Test
    void saveTest(){
        SiteUser register = userH2Repository.save(new SiteUser("user1", "password", "email@email.com"));
        Question q = new Question("hi", "hihi", register);
        Question save = questionH2Repository.save(q);

        Assertions.assertThat(save).isNotNull();
        Assertions.assertThat(save.getId()).isNotNull();
    }
    @Test
    @DisplayName("question 삭제 시 comment, answer가 모두 삭제되어야 한다")
    void deleteTest(){
        SiteUser register = userH2Repository.save(new SiteUser("user1", "password", "email@email.com"));
        Question q = new Question("hi", "hihi", register);
        Question save = questionH2Repository.save(q);

        Answer savedAnswer = answerH2Repository.save(new Answer(save, "con", register));
        CommentForm commentForm = new CommentForm();
        commentForm.setContent("hi");
        Comment savedComment = commentH2Repository.save(Comment.builder()
                .content("con")
                .targetQuestion(save)
                .author(register)
                .build());

        questionH2Repository.deleteById(save.getId());
        Assertions.assertThat(questionH2Repository.findById(save.getId())).isEmpty();
        Assertions.assertThat(commentH2Repository.findById(savedComment.getId())).isEmpty();
        Assertions.assertThat(answerH2Repository.findById(savedAnswer.getId())).isEmpty();
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