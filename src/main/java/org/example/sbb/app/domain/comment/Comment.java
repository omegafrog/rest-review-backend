package org.example.sbb.app.domain.comment;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.sbb.app.domain.question.answer.Answer;
import org.example.sbb.app.domain.question.question.Question;
import org.example.sbb.app.domain.user.SiteUser;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne
    private SiteUser author;

    @ManyToOne
    private Question targetQuestion;

    @ManyToOne
    private Answer targetAnswer;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Builder
    public Comment(String content, SiteUser author, Question targetQuestion, Answer targetAnswer) {
        this.content = content;
        this.author = author;
        this.targetQuestion = targetQuestion;
        this.targetAnswer = targetAnswer;

        if(targetQuestion!=null)
            targetQuestion.getComments().add(this);
        if(targetAnswer!=null)
            targetAnswer.getComments().add(this);
        author.getWroteComments().add(this);
    }
}
