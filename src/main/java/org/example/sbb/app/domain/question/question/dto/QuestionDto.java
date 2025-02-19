package org.example.sbb.app.domain.question.question.dto;

import org.example.sbb.app.domain.comment.dto.CommentDto;
import org.example.sbb.app.domain.user.dto.SiteUserDto;
import org.example.sbb.app.domain.question.answer.dto.AnswerDto;
import org.example.sbb.app.domain.question.question.Question;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public record QuestionDto(Long id,
                          String subject,
                          String content,
                          SiteUserDto author,
                          Page<AnswerDto> answers,
                          List<SiteUserDto> voters,
                          Page<CommentDto> comments,
                          Integer viewCount,
                          LocalDateTime createdAt,
                          LocalDateTime modifiedAt) {

    public static QuestionDto of(Question question, Page<AnswerDto> answers, Page<CommentDto> comments) {
        return new QuestionDto(
                question.getId(),
                question.getSubject(),
                question.getContent(),
                question.getAuthor() != null ?SiteUserDto.of(question.getAuthor()):null,
                answers,
                question.getVoters().stream().map(
                        questionVoter -> SiteUserDto.of(questionVoter.getVoter())
                ).toList(),
                comments,
                question.getViewCount(),
                question.getCreatedAt(),
                question.getModifiedAt());
    }
}
