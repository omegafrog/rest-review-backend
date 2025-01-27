package org.example.sbb.app.domain.question.question;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.sbb.app.domain.comment.Comment;
import org.example.sbb.app.domain.question.answer.Answer;
import org.example.sbb.app.domain.question.recommend.QuestionVoter;
import org.example.sbb.app.domain.user.SiteUser;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "QUESTION")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "subject")
    private String subject;
    @Column(name = "content")
    private String content;
    @OneToMany(mappedBy = "question" )
    private final List<QuestionVoter> voters = new ArrayList<>();
    @OneToMany(mappedBy = "targetQuestion", cascade=CascadeType.REMOVE, orphanRemoval = true)
    private final List<Comment> comments = new ArrayList<>();
    @Column(name="view_count")
    private Integer viewCount;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;
    @Column(name = "modified_at")
    @LastModifiedDate
    private LocalDateTime modifiedAt;
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private final List<Answer> answers = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "author_id")
    private SiteUser author;

    public Question(String subject, String content, SiteUser author) {
        this.subject = subject;
        this.content = content;
        this.author = author;
        this.viewCount = 0;
        author.addWroteQuestion(this);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public Question update(String updatedSubject, String updatedContent) {
        this.subject = updatedSubject;
        this.content = updatedContent;
        this.modifiedAt = LocalDateTime.now();
        return this;
    }

    public QuestionVoter vote(SiteUser voter) {
        QuestionVoter questionVoter = new QuestionVoter(voter, this);
        voters.add(questionVoter);
        voter.getVotedQuestions().add(questionVoter);
        return questionVoter;
    }

    public synchronized int increaseViewCount() {
        return this.viewCount+=1;
    }

    @Override
    public String toString() {
        String collect = voters.stream().map(voters -> voters.getVoter().toString()).collect(Collectors.joining(", "));
        return "Question{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", voters=" + collect +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", answers=" + answers +
                ", author=" + author +
                '}';
    }
}



