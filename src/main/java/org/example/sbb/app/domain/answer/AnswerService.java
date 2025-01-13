package org.example.sbb.app.domain.answer;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.sbb.app.domain.dto.AnswerDto;
import org.example.sbb.app.domain.question.Question;
import org.example.sbb.app.domain.question.QuestionH2Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AnswerService {

    private final AnswerH2Repository answerRepository;
    private final QuestionH2Repository questionRepository;

    public void writeAnswer(Long questionId, String content) {
        Question founded = questionRepository.findById(questionId).orElseThrow(EntityNotFoundException::new);

        Answer answer = new Answer(founded, content);
        answerRepository.save(answer);
    }

    public List<AnswerDto> getAnswerList(Long questionId){
        Question question = questionRepository.findById(questionId).orElseThrow(EntityNotFoundException::new);
        return question.getAnswers().stream().map(AnswerDto::of).collect(Collectors.toList());
    }
}
