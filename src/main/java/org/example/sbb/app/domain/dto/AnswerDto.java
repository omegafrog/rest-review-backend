package org.example.sbb.app.domain.dto;

import org.example.sbb.app.domain.answer.Answer;

import java.time.LocalDateTime;
import java.util.List;

public record AnswerDto(Long id,
                        String content,
                        LocalDateTime createdAt,
                        LocalDateTime modifiedAt,
                        List<SiteUserDto> voters,
                        SiteUserDto author) {

    public static AnswerDto of(Answer answer){
        return new AnswerDto(answer.getId(),
                answer.getContent(),
                answer.getCreatedAt(),
                answer.getModifiedAt(),
                answer.getVoters().stream().map(answerVoter->SiteUserDto.of(answerVoter.getVoter())).toList(),
                SiteUserDto.of(answer.getAuthor()));
    }
}
