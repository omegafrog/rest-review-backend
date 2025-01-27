package org.example.sbb.app.domain.question.question.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class QuestionForm {
    @NotEmpty
    @Size( max = 50)
    private String subject;
    @NotEmpty
    @Size( max=500)
    private String content;
}
