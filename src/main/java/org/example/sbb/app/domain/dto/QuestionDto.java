package org.example.sbb.app.domain.dto;

import org.example.sbb.app.domain.question.Question;

import java.time.LocalDateTime;

public record QuestionDto(Long id, String subject, String content, SiteUserDto author, LocalDateTime createdAt) {

    public static QuestionDto of(Question question) {
        return new QuestionDto(
                question.getId(),
                question.getSubject(),
                question.getContent(),
                SiteUserDto.of(question.getAuthor()),
                question.getCreatedAt());
    }
}
