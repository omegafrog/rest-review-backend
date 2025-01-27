package org.example.sbb.app.domain.comment.dto;

import org.example.sbb.app.domain.comment.Comment;
import org.example.sbb.app.domain.user.dto.SiteUserDto;

import java.time.LocalDateTime;

public record  CommentDto(Long id, String content, SiteUserDto author, LocalDateTime createdAt,
                          LocalDateTime modifiedAt) {

    public static CommentDto of(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getContent(),
                SiteUserDto.of(comment.getAuthor()),
                comment.getCreatedAt(),
                comment.getModifiedAt());
    }
}
