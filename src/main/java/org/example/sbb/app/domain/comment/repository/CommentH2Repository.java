package org.example.sbb.app.domain.comment.repository;

import org.example.sbb.app.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentH2Repository extends JpaRepository<Comment, Long> {
}
