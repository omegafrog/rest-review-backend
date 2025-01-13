package org.example.sbb.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "ANSWER")
@NoArgsConstructor
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
    private final LocalDateTime createdAt = LocalDateTime.now();

    public Answer( Question question, String content) {
        this.question = question;
        this.content = content;
    }
}
