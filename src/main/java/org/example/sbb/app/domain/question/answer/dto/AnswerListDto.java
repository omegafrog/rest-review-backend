package org.example.sbb.app.domain.question.answer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.sbb.app.domain.question.question.dto.QuestionDto;
import org.example.sbb.app.domain.user.dto.SiteUserDto;
import org.example.sbb.app.domain.question.answer.Answer;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerListDto {
    private Long id;
    private String content;
    private QuestionDto target;
    private SiteUserDto author;
    private Integer voterCount;
    private LocalDateTime createdAt;

    public static AnswerListDto of(Answer answer) {
        return new AnswerListDto(answer.getId(),
                answer.getContent(),
                QuestionDto.of(answer.getQuestion(), null, null),
                SiteUserDto.of(answer.getAuthor()),
                answer.getVoters().size(),
                answer.getCreatedAt());
    }
}
