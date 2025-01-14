package org.example.sbb.app.domain.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.sbb.app.domain.answer.Answer;
import org.example.sbb.app.domain.question.Question;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name="MEMBER")
@NoArgsConstructor
public class SiteUser {
    @Id
    @Column(unique = true)
    private String id;
    private String password;
    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Question> wroteQuestions = new ArrayList<>();

    @OneToMany(mappedBy = "author",cascade = CascadeType.REMOVE)
    private List<Answer> wroteAnswers = new ArrayList<>();

    public SiteUser(String id, String password, String email) {
        this.id = id;
        this.password = password;
        this.email = email;
    }

    public void addWroteQuestion(Question question) {
        wroteQuestions.add(question);
    }

    public void addWroteAnswers(Answer answer) {
        wroteAnswers.add(answer);
    }
}
