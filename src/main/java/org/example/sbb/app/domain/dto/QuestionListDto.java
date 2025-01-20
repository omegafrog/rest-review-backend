package org.example.sbb.app.domain.dto;

import org.example.sbb.app.domain.question.Question;

import java.time.LocalDateTime;

public record QuestionListDto(Long id,
                              String subject,
                              SiteUserDto author,
                              Integer answerCount,
                              Integer voterCount,
                              LocalDateTime createdAt) {

    public static QuestionListDto of(Question question) {
        return new QuestionListDto(
                question.getId(),
                question.getSubject(),
                question.getAuthor() != null ? SiteUserDto.of(question.getAuthor()) : null,
                question.getAnswers().size(),
                question.getVoters().size(),
                question.getCreatedAt());
    }
}

