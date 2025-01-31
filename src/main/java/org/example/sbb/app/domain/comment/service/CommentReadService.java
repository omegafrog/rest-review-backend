package org.example.sbb.app.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.example.sbb.app.domain.comment.Comment;
import org.example.sbb.app.domain.comment.repository.QueryDslCommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentReadService {
    private final QueryDslCommentRepository commentRepository;
    public Page<Comment> comments(Long questionId, Pageable pageable) {
        return commentRepository.findAllByQuestion(questionId, pageable);
    }
}
