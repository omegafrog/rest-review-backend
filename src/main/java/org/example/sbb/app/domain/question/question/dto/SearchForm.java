package org.example.sbb.app.domain.question.question.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchForm {
    private String keyword;
    private Integer pageNum;
}
