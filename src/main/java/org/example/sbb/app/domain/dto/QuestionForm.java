package org.example.sbb.app.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@RequiredArgsConstructor
public class QuestionForm {
    @NotEmpty
    @Size( max = 50)
    private final String subject;
    @NotEmpty
    @Size( max=500)
    private final String content;
}
