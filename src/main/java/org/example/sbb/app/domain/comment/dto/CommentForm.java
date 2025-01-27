package org.example.sbb.app.domain.comment.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentForm {
    @NotEmpty
    private String content;
}
