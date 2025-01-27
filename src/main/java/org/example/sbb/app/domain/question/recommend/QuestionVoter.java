package org.example.sbb.app.domain.question.recommend;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.sbb.app.domain.question.question.Question;
import org.example.sbb.app.domain.user.SiteUser;

@Entity
@Getter
@NoArgsConstructor
@IdClass(QuestionVoterId.class)
@AllArgsConstructor
public class QuestionVoter {

    @Id
    @ManyToOne
    @JoinColumn(name="VOTER_ID")
    private SiteUser voter;

    @Id
    @ManyToOne
    @JoinColumn(name="QUESTION_ID")
    private Question question;
}
