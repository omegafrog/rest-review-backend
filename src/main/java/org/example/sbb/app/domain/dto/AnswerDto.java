package org.example.sbb.app.domain.dto;

import org.example.sbb.app.domain.answer.Answer;

import java.time.LocalDateTime;

public record AnswerDto(Long id, String content, LocalDateTime createdAt, Long questionId) {

    public static AnswerDto of(Answer answer){
        return new AnswerDto(answer.getId(), answer.getContent(), answer.getCreatedAt(), answer.getQuestion().getId());
    }
}
