package org.example.sbb.app.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.sbb.app.domain.user.SiteUser;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SiteUserInfoDto {
    private String id;
    private String email;
    private List<QuestionListDto> wroteQuestion;
    private List<AnswerListDto> wroteAnswer;
    private List<CommentListDto> wroteComment;
    private LocalDateTime createdAt;

    public static SiteUserInfoDto of(SiteUser user) {
        return new SiteUserInfoDto(user.getId(),
                user.getEmail(),
                user.getWroteQuestions().stream().map(QuestionListDto::of).toList(),
                user.getWroteAnswers().stream().map(AnswerListDto::of).toList(),
                user.getWroteComments().stream().map(CommentListDto::of).toList(),
                user.getCreatedAt());
    }

}
