package org.example.sbb.app.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.sbb.app.domain.comment.Comment;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentListDto {
    private Long id;
    private String content;
    private QuestionDto targetQuestion;
    private AnswerDto targetAnswer;
    private LocalDateTime createdAt;

    public static CommentListDto of(Comment comment) {
        return new CommentListDto(
                comment.getId(),
                comment.getContent(),
                comment.getTargetQuestion() != null ? QuestionDto.of(comment.getTargetQuestion(), null, null) : null,
                comment.getTargetAnswer() != null ? AnswerDto.of(comment.getTargetAnswer()) : null,
                comment.getCreatedAt()
        );
    }
}

