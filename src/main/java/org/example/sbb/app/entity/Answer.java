package org.example.sbb.app.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity(name = "ANSWER")
@Getter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    @Column(name="content")
    private String content;
    @Column(name="created_at")
    private LocalDateTime createdAt;
}
