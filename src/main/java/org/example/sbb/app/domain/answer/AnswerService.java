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
import org.example.sbb.app.domain.relation.AnswerVoter;
import org.example.sbb.app.domain.relation.AnswerVoterRepository;
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
    private final AnswerVoterRepository answerVoterRepository;

    public AnswerDto writeAnswer(Long questionId, String content, Authentication auth) {
        Question founded = questionRepository.findById(questionId).orElseThrow(EntityNotFoundException::new);

        SiteUser author = userRepository.findById(getUsername(auth))
                .orElseThrow(EntityNotFoundException::new);
        Answer answer = new Answer(founded, content, author);
        return AnswerDto.of(answerRepository.save(answer));
    }

    private static String getUsername(Authentication auth) {
        return ((User) auth.getPrincipal()).getUsername();
    }

    public List<AnswerDto> getAnswerList(Long questionId){
        Question question = questionRepository.findById(questionId).orElseThrow(EntityNotFoundException::new);
        return question.getAnswers().stream().map(AnswerDto::of).collect(Collectors.toList());
    }

    public void prepareAnswerForm(Long answerId, AnswerForm form, Authentication auth) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new EntityNotFoundException(answerId + "인 답변이 없습니다."));

        isAuthor(auth, answer);
        form.setContent(answer.getContent());
    }

    public AnswerDto modify(Long answerId, @NotEmpty @Size( max = 500) String content, Authentication auth) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(EntityNotFoundException::new);
        isAuthor(auth, answer);
        return AnswerDto.of(answer.modify(content));
    }
    public void delete(Long answerId, Authentication auth) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(EntityNotFoundException::new);
        isAuthor(auth, answer);
        answerRepository.deleteById(answerId);
    }

    private static void isAuthor(Authentication auth, Answer answer) {
        if(!answer.getAuthor().getId().equals(((User) auth.getPrincipal()).getUsername()))
            throw new BadCredentialsException("작성자가 아닙니다.");
    }

    public AnswerDto recommend(Long answerId, Authentication auth) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(EntityNotFoundException::new);
        SiteUser voter = userRepository.findById(getUsername(auth)).orElseThrow(EntityNotFoundException::new);
        if(answer.getAuthor().equals(voter))
            throw new RuntimeException("자신의 글을 추천할 수 없습니다.");
        AnswerVoter relation = answer.vote(voter);
        answerVoterRepository.save(relation);
        return AnswerDto.of(answer);
    }
}
