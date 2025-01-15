package org.example.sbb.app.domain.answer;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.example.sbb.app.domain.dto.AnswerDto;
import org.example.sbb.app.domain.dto.AnswerForm;
import org.example.sbb.app.domain.question.Question;
import org.example.sbb.app.domain.question.QuestionH2Repository;
import org.example.sbb.app.domain.user.SiteUser;
import org.example.sbb.app.domain.user.UserH2Repository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AnswerService {

    private final AnswerH2Repository answerRepository;
    private final QuestionH2Repository questionRepository;
    private final UserH2Repository userRepository;

    public void writeAnswer(Long questionId, String content, Authentication auth) {
        Question founded = questionRepository.findById(questionId).orElseThrow(EntityNotFoundException::new);

        SiteUser author = userRepository.findById(getUsername(auth))
                .orElseThrow(EntityNotFoundException::new);
        Answer answer = new Answer(founded, content, author);
        answerRepository.save(answer);
    }

    private static String getUsername(Authentication auth) {
        return ((User) auth.getPrincipal()).getUsername();
    }

    public List<AnswerDto> getAnswerList(Long questionId){
        Question question = questionRepository.findById(questionId).orElseThrow(EntityNotFoundException::new);
        return question.getAnswers().stream().map(AnswerDto::of).collect(Collectors.toList());
    }

    public boolean existAnswer(Long answerId) {
        return answerRepository.existsById(answerId);
    }

    public void prepareAnswerForm(Long answerId, AnswerForm form, Authentication auth) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new EntityNotFoundException(answerId + "인 답변이 없습니다."));

        if(!answer.getAuthor().getId().equals(((User)auth.getPrincipal()).getUsername()))
            throw new BadCredentialsException("작성자가 아닙니다.");
        form.setContent(answer.getContent());
    }

    public void modify(Long answerId, @NotEmpty @Size( max = 500) String content, Authentication auth) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(EntityNotFoundException::new);
        if(!answer.getAuthor().getId().equals(((User)auth.getPrincipal()).getUsername()))
            throw new BadCredentialsException("작성자가 아닙니다.");
        answer.modify(content);
    }
}
