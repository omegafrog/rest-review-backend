package org.example.sbb.app.domain.question.recommend;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.sbb.app.domain.question.answer.Answer;
import org.example.sbb.app.domain.user.SiteUser;

@Entity
@IdClass(AnswerVoterId.class)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerVoter {

    @Id
    @ManyToOne
    @JoinColumn(name="ANSWER_ID")
    private Answer answer;

    @Id
    @ManyToOne
    @JoinColumn(name="VOTER_ID")
    private SiteUser voter;

}
