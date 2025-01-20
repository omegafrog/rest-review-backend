package org.example.sbb.app.domain.comment;

import jakarta.persistence.*;
import lombok.*;
import org.example.sbb.app.domain.answer.Answer;
import org.example.sbb.app.domain.question.Question;
import org.example.sbb.app.domain.user.SiteUser;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne
    private SiteUser author;

    @ManyToOne
    private Question targetQuestion;

    @ManyToOne
    private Answer targetAnswer;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;


}
