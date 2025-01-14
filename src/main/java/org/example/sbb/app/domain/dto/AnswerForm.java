package org.example.sbb.app.domain.dto;

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
    private final String content;
}
