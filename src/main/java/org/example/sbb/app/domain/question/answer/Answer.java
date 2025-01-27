package org.example.sbb.app.domain.question.answer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.sbb.app.domain.comment.Comment;
import org.example.sbb.app.domain.question.question.Question;
import org.example.sbb.app.domain.question.recommend.AnswerVoter;
import org.example.sbb.app.domain.user.SiteUser;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "ANSWER")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
    @Column(name="content")
    private String content;
    @OneToMany(mappedBy = "answer")
    private final List<AnswerVoter> voters = new ArrayList<>();
    @OneToMany(mappedBy = "targetAnswer")
    private final List<Comment> comments = new ArrayList<>();
    @Column(name="created_at")
    @CreatedDate
    private LocalDateTime createdAt;
    @Column(name="modified_at")
    @LastModifiedDate
    private LocalDateTime modifiedAt;
    @ManyToOne
    @JoinColumn(name="author_id")
    private SiteUser author;

    public Answer( Question question, String content, SiteUser author) {
        this.question = question;
        this.content = content;
        question.addAnswer(this);
        this.author = author;
        author.addWroteAnswers(this);
    }

    public Answer modify(@NotEmpty @Size(max = 500) String content) {
        this.content = content;
        this.modifiedAt = LocalDateTime.now();
        return this;
    }

    public AnswerVoter vote(SiteUser voter) {
        AnswerVoter answerVoter = new AnswerVoter(this, voter);
        voters.add(answerVoter);
        voter.getVotedAnswers().add(answerVoter);
        return answerVoter;
    }
}
