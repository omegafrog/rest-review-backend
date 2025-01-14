package org.example.sbb.app.domain.question;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.sbb.app.domain.dto.QuestionDto;
import org.example.sbb.app.domain.user.SiteUser;
import org.example.sbb.app.domain.user.UserH2Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public List<Question> getQuestionList() {
        return repository.findAll();
    }

    public Page<Question> getQuestionPage(Pageable pageable) {
        Pageable newPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.desc("createdAt")));
        return repository.findAll(newPage);
    }

    public QuestionDto getQuestionInfo(Long id) {
        return QuestionDto.of(repository.findById(id)
                .orElseThrow(EntityNotFoundException::new));
    }

    public void writeQuestion(String subject, String content, Authentication auth) {

        SiteUser author= userRepository.findById( ((User)auth.getPrincipal()).getUsername())
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

    private String getId(Authentication auth){
        return ((User) auth.getPrincipal()).getUsername();
    }

    public void delete(Long id, Authentication auth) {
        Question question = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        if(!question.getAuthor().getId().equals(getId(auth)))
            throw new UsernameNotFoundException(question.getAuthor().getId() + "인 유저를 찾을 수 없습니다.");

        repository.deleteById(id);
    }
}
