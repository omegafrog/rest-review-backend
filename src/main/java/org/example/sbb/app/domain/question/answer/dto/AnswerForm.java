package org.example.sbb.app.domain.question.answer.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class AnswerForm {
    @NotEmpty
    @Size( max = 500)
    private String content;
}
