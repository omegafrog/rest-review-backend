package org.example.sbb.app.domain.dto;

import org.example.sbb.app.domain.question.Question;

import java.time.LocalDateTime;
import java.util.List;

public record QuestionDto(Long id,
                          String subject,
                          String content,
                          SiteUserDto author,
                          List<AnswerDto> answers,
                          List<SiteUserDto> voters,
                          LocalDateTime createdAt,
                          LocalDateTime modifiedAt) {

    public static QuestionDto of(Question question) {
        return new QuestionDto(
                question.getId(),
                question.getSubject(),
                question.getContent(),
                question.getAuthor() != null ?SiteUserDto.of(question.getAuthor()):null,
                question.getAnswers().stream().map(AnswerDto::of).toList(),
                question.getVoters().stream().map(
                        questionVoter -> SiteUserDto.of(questionVoter.getVoter())
                ).toList(),
                question.getCreatedAt(),
                question.getModifiedAt());
    }
}
