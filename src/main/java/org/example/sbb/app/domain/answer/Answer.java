package org.example.sbb.app.domain.answer;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.sbb.app.domain.question.Question;
import org.example.sbb.app.domain.relation.AnswerVoter;
import org.example.sbb.app.domain.user.SiteUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "ANSWER")
@Getter
@NoArgsConstructor
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
    @Column(name="created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();
    @Column(name="modified_at")
    private LocalDateTime modifiedAt=LocalDateTime.now();
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

    public void modify(@NotEmpty @Size(max = 500) String content) {
        this.content = content;
        this.modifiedAt = LocalDateTime.now();
    }

    public AnswerVoter vote(SiteUser voter) {
        AnswerVoter answerVoter = new AnswerVoter(this, voter);
        voters.add(answerVoter);
        voter.getVotedAnswers().add(answerVoter);
        return answerVoter;
    }
}
