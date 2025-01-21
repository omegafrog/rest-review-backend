package org.example.sbb.app.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.sbb.app.domain.answer.Answer;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerListDto {
    private Long id;
    private String content;
    private QuestionDto target;
    private LocalDateTime createdAt;

    public static AnswerListDto of(Answer answer) {
        return new AnswerListDto(answer.getId(),
                answer.getContent(),
                QuestionDto.of(answer.getQuestion(), null, null),
                answer.getCreatedAt());
    }
}
