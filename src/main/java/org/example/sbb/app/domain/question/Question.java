package org.example.sbb.app.domain.question;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.sbb.app.entity.Answer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name="QUESTION")
@Getter
@NoArgsConstructor
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "subject")
    private String subject;
    @Column(name="content")
    private String content;
    @Column(name="created_at")
    private LocalDateTime createdAt=LocalDateTime.now();
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answers = new ArrayList<>();

    public Question( String subject, String content) {
        this.subject = subject;
        this.content = content;
    }
}


