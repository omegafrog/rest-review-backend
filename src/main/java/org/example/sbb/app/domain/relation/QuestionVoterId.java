package org.example.sbb.app.domain.relation;

import java.io.Serializable;
import java.util.Objects;

public class QuestionVoterId implements Serializable {
    private String voter;
    private Long question;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof QuestionVoterId that)) return false;
        return Objects.equals(voter, that.voter) && Objects.equals(question, that.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voter, question);
    }
}
