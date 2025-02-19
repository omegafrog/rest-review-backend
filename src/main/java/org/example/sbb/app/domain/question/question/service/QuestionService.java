package org.example.sbb.app.domain.question.question.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.sbb.app.domain.comment.dto.CommentDto;
import org.example.sbb.app.domain.comment.service.CommentReadService;
import org.example.sbb.app.domain.question.answer.Answer;
import org.example.sbb.app.domain.question.answer.SortOption;
import org.example.sbb.app.domain.question.answer.dto.AnswerDto;
import org.example.sbb.app.domain.question.answer.repository.QueryDslAnswerRepository;
import org.example.sbb.app.domain.question.question.Question;
import org.example.sbb.app.domain.question.question.dto.QuestionDto;
import org.example.sbb.app.domain.question.question.dto.QuestionListDto;
import org.example.sbb.app.domain.question.question.repository.QueryDslQuestionH2Repository;
import org.example.sbb.app.domain.question.question.repository.QuestionH2Repository;
import org.example.sbb.app.domain.question.recommend.QuestionVoter;
import org.example.sbb.app.domain.question.recommend.QuestionVoterRepository;
import org.example.sbb.app.domain.user.SiteUser;
import org.example.sbb.app.domain.user.UserService;
import org.example.sbb.app.global.aop.AbstractService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService extends AbstractService {

    private final QuestionH2Repository repository;
    private final QuestionVoterRepository questionVoterRepository;
    private final QueryDslQuestionH2Repository queryDslQuestionH2Repository;
    private final QueryDslAnswerRepository answerRepository;
    private final UserService userService;
    private final CommentReadService commentService;

    @Setter
    private String userId;

    public Page<QuestionListDto> getQuestionPage(String keyword, Pageable pageable) {
        Pageable newPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.desc("createdAt")));
        return repository.findAll(search(keyword), newPage).map(QuestionListDto::of);
    }
    public Page<QuestionListDto> getQuestionPage(String keyword, SortOption sortOption, Pageable pageable) {
        return queryDslQuestionH2Repository.findAll(keyword, pageable, sortOption);
    }

    public QuestionDto getQuestionInfo(Long id, SortOption option, Pageable answerPageable, Pageable commentPageable) {
        Question question = repository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Question not found"));
        question.increaseViewCount();
        Page<AnswerDto> answerPage = answerRepository.findAllByQuestionId(id, answerPageable, option)
                .map(AnswerDto::of);
        Page<CommentDto> comments = commentService.comments(id, commentPageable).map(CommentDto::of);
        return QuestionDto.of(question, answerPage, comments);
    }

    public QuestionDto getQuestionInfo(Long id, Pageable answerPageable, Pageable commentPageable) {
        SortOption option = SortOption.of("time");
        return getQuestionInfo(id, option, answerPageable, commentPageable);
    }
    public void writeQuestion(String subject, String content) {
        SiteUser author = userService.findUserById(userId);
        Question question = new Question(subject, content, author);
        repository.save(question);
    }

    public void modify(Long id, String updatedSubject, String updatedContent) {
        Question question = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        question.update(updatedSubject, updatedContent);
        if (!question.getAuthor().getId().equals(userId))
            throw new UsernameNotFoundException(question.getAuthor().getId() + "인 유저를 찾을 수 없습니다.");
    }

    private String getId(Authentication auth) {
        if(auth.getPrincipal() instanceof User)
            return ((User) auth.getPrincipal()).getUsername();
        else
            throw new AccessDeniedException("Login needed.");
    }

    public void delete(Long id) {
        Question question = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        if (!question.getAuthor().getId().equals(userId))
            throw new UsernameNotFoundException(question.getAuthor().getId() + "인 유저를 찾을 수 없습니다.");

        repository.deleteById(id);
    }

    public void recommend(Long id) {
        Question question = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        SiteUser voter = userService.findUserById(userId);

        if (question.getAuthor().equals(voter))
            throw new RuntimeException("자신의 글은 추천할 수 없습니다.");

        if (question.getVoters().stream().anyMatch(questionVoter ->
                questionVoter.getQuestion().equals(question) && questionVoter.getVoter().equals(voter)))
            throw new RuntimeException("이미 추천을 누른 질문입니다.");
        QuestionVoter relation = question.vote(voter);
        questionVoterRepository.save(relation);
    }

    private Specification<Question> search(String keyword) {
        return new Specification<Question>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Question> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);

                Join<Question, SiteUser> u1 = root.join("author", JoinType.LEFT);
                Join<Question, Answer> a = root.join("answers", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(root.get("subject"), "%" + keyword + "%"), // 제목
                        cb.like(root.get("content"), "%" + keyword + "%"),      // 내용
                        cb.like(u1.get("id"), "%" + keyword + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + keyword + "%"),      // 답변 내용
                        cb.like(u2.get("id"), "%" + keyword + "%"));   // 답변 작성자
            }
        };
    }

    public Question getQuestion(Long questionId) {
        return repository.findById(questionId).orElseThrow(EntityNotFoundException::new);
    }
}
