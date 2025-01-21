package org.example.sbb.app.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentH2Repository extends JpaRepository<Comment, Long> {
}
