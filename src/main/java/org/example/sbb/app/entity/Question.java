package org.example.sbb.app.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name="QUESTION")
@Getter
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "subject")
    private String subject;
    @Column(name="content")
    private String content;
    @Column(name="created_at")
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answers;
}
