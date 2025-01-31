package org.example.sbb.app.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.example.sbb.app.domain.comment.Comment;
import org.example.sbb.app.domain.comment.dto.CommentForm;
import org.example.sbb.app.domain.comment.repository.CommentH2Repository;
import org.example.sbb.app.domain.question.answer.Answer;
import org.example.sbb.app.domain.question.answer.service.AnswerService;
import org.example.sbb.app.domain.question.question.Question;
import org.example.sbb.app.domain.question.question.service.QuestionReadService;
import org.example.sbb.app.domain.user.SiteUser;
import org.example.sbb.app.domain.user.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService{

    private final CommentH2Repository commentH2Repository;
    private final UserService userService;
    private final QuestionReadService questionService;
    private final AnswerService answerService;

    private String userId;

    public void writeComment(CommentForm form, String targetName, Long targetId) {
        SiteUser author= userService.findUserById(userId);

        Comment.CommentBuilder commentBuilder = switch (targetName) {
            case "questions" -> writeToQuestion(targetId);
            case "answers" -> writeToAnswer(targetId);
            default -> null;
        };
        Comment comment = commentBuilder.
                content(form.getContent())
                .author(author)
                .build();

        commentH2Repository.save(comment);
    }

    private Comment.CommentBuilder writeToAnswer(Long targetId) {
        Answer answer = answerService.getAnswer(targetId);
        return Comment.builder()
                .targetAnswer(answer);
    }

    private Comment.CommentBuilder writeToQuestion(Long targetId) {
        Question question = questionService.getQuestion(targetId);
        return Comment.builder()
                .targetQuestion(question);
    }
}
