package org.example.sbb.app.domain.question.answer.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.example.sbb.app.domain.question.answer.Answer;
import org.example.sbb.app.domain.question.answer.SortOption;
import org.example.sbb.app.domain.question.answer.dto.AnswerDto;
import org.example.sbb.app.domain.question.answer.dto.AnswerForm;
import org.example.sbb.app.domain.question.answer.dto.AnswerListDto;
import org.example.sbb.app.domain.question.answer.repository.AnswerH2Repository;
import org.example.sbb.app.domain.question.answer.repository.QueryDslAnswerRepository;
import org.example.sbb.app.domain.question.question.Question;
import org.example.sbb.app.domain.question.question.service.QuestionService;
import org.example.sbb.app.domain.question.recommend.AnswerVoter;
import org.example.sbb.app.domain.question.recommend.AnswerVoterRepository;
import org.example.sbb.app.domain.user.SiteUser;
import org.example.sbb.app.domain.user.UserService;
import org.example.sbb.app.global.aop.AbstractService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AnswerService extends AbstractService {

    private final AnswerH2Repository answerRepository;
    private final AnswerVoterRepository answerVoterRepository;
    private final QueryDslAnswerRepository queryDslAnswerRepository;
    private final QuestionService questionService;
    private final UserService userService;

    public AnswerDto writeAnswer(Long questionId, String content) {
        // TODO : service layer에서는 dto를 리턴하는데, 다른 도메인의 서비스에서 엔티티 객체 자체가 필요한 경우가 있다. 이 경우 어떻게하는가?
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        Question founded = questionService.getQuestion(questionId);
        SiteUser author = userService.findUserById(getUsername(auth));
        Answer answer = new Answer(founded, content, author);
        return AnswerDto.of(answerRepository.save(answer));
    }

    public List<AnswerDto> getAnswerList(Long questionId){
        Question question= questionService.getQuestion(questionId);
        return question.getAnswers().stream().map(AnswerDto::of).collect(Collectors.toList());
    }

    public Page<AnswerDto> getAnswerList(Long questionId, Pageable pageable, SortOption sortOption){
        return queryDslAnswerRepository.findAllByQuestionId(questionId, pageable, sortOption).map(AnswerDto::of);
    }

    public void prepareAnswerForm(Long answerId, AnswerForm form) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException(answerId + "인 답변이 없습니다."));

        if(!isAuthor(answer))
            throw new BadCredentialsException("자신의 글이 아닙니다.");

        form.setContent(answer.getContent());
    }

    public AnswerDto modify(Long answerId, @NotEmpty @Size( max = 500) String content) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(EntityNotFoundException::new);
        if(isAuthor( answer))
            throw new BadCredentialsException("자신의 글이 아닙니다.");

        return AnswerDto.of(answer.modify(content));
    }

    public void delete(Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(EntityNotFoundException::new);
        if(isAuthor( answer))
            throw new BadCredentialsException("자신의 글이 아닙니다.");
        answerRepository.deleteById(answerId);
    }


    public AnswerDto recommend(Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(EntityNotFoundException::new);
        SiteUser voter = userService.findUserById(userId);

        if(isAuthor(answer))
            throw new RuntimeException("자신의 글을 추천할 수 없습니다.");

        AnswerVoter relation = answer.vote(voter);
        answerVoterRepository.save(relation);
        return AnswerDto.of(answer);
    }

    private boolean isAuthor(Answer answer) {
        return !answer.getAuthor().getId().equals(userId);
    }

    private static String getUsername(Authentication auth) {
        return ((User) auth.getPrincipal()).getUsername();
    }

    public Answer getAnswer(Long targetId) {
        return answerRepository.findById(targetId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find answer entity. "+targetId));
    }

    public Page<AnswerListDto> getAnswerPage(Pageable pageable) {
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));
        return answerRepository.findAll(newPageable).map(AnswerListDto::of);
    }
}
