package org.example.sbb.app.domain.question;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.sbb.app.domain.answer.Answer;
import org.example.sbb.app.domain.answer.AnswerH2Repository;
import org.example.sbb.app.domain.dto.AnswerDto;
import org.example.sbb.app.domain.dto.QuestionDto;
import org.example.sbb.app.domain.dto.QuestionListDto;
import org.example.sbb.app.domain.relation.QuestionVoter;
import org.example.sbb.app.domain.relation.QuestionVoterRepository;
import org.example.sbb.app.domain.user.SiteUser;
import org.example.sbb.app.domain.user.UserH2Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {

    private final QuestionH2Repository repository;
    private final UserH2Repository userRepository;
    private final AnswerH2Repository answerRepository;
    private final QuestionVoterRepository questionVoterRepository;

    public Page<QuestionListDto> getQuestionPage(String keyword, Pageable pageable) {
        Pageable newPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.desc("createdAt")));
        return repository.findAll(search(keyword), newPage).map(QuestionListDto::of);
    }

    public QuestionDto getQuestionInfo(Long id, Pageable pageable) {
        Page<Answer> all = answerRepository.findAll(pageable);
        Page<AnswerDto> map = all.map(AnswerDto::of);
        return QuestionDto.of(repository.findById(id)
                .orElseThrow(EntityNotFoundException::new), map);
    }

    public void writeQuestion(String subject, String content, Authentication auth) {

        SiteUser author = userRepository.findById(((User) auth.getPrincipal()).getUsername())
                .orElseThrow(EntityNotFoundException::new);
        Question question = new Question(subject, content, author);
        repository.save(question);
    }

    public void modify(Long id, String updatedSubject, String updatedContent, Authentication auth) {
        Question question = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        question.update(updatedSubject, updatedContent);
        if (!question.getAuthor().getId().equals(getId(auth)))
            throw new UsernameNotFoundException(question.getAuthor().getId() + "인 유저를 찾을 수 없습니다.");
    }

    private String getId(Authentication auth) {
        return ((User) auth.getPrincipal()).getUsername();
    }

    public void delete(Long id, Authentication auth) {
        Question question = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        if (!question.getAuthor().getId().equals(getId(auth)))
            throw new UsernameNotFoundException(question.getAuthor().getId() + "인 유저를 찾을 수 없습니다.");

        repository.deleteById(id);
    }

    public void recommend(Long id, Authentication auth) {
        Question question = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        SiteUser voter = userRepository.findById(getId(auth))
                .orElseThrow(EntityNotFoundException::new);

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
}
