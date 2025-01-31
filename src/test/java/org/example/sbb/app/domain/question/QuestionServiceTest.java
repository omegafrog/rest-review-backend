package org.example.sbb.app.domain.question;

import org.assertj.core.api.Assertions;
import org.example.sbb.app.domain.question.question.Question;
import org.example.sbb.app.domain.question.question.dto.QuestionListDto;
import org.example.sbb.app.domain.question.question.repository.QuestionH2Repository;
import org.example.sbb.app.domain.question.question.service.QuestionService;
import org.example.sbb.app.domain.user.SiteUser;
import org.example.sbb.app.domain.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionH2Repository repository;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private UserService userService;

    @InjectMocks
    private QuestionService questionService;

    private Long questionId = 1L;
    private String authorId="user";
    @BeforeEach
    void setUp(){
        Mockito.when(securityContext.getAuthentication())
                .thenReturn(new UsernamePasswordAuthenticationToken(new User(
                        "user",
                        "encodedPassword",
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))), "encodedPassword",
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))));

        SiteUser author = new SiteUser("user", "encodedPassword", "email@email.com");
        Question question = new Question("sub", "content", author);

        Mockito.when(userService.findUserById("user"))
                .thenReturn(author);

        Mockito.when(repository.save(Mockito.any(Question.class)))
                .thenAnswer(invocation -> {
                    ReflectionTestUtils.setField(question, "id", questionId);
                    return question;
                });

        Mockito.when(repository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(question),
                        PageRequest.of(0, 5, Sort.by(Sort.Order.desc("createdAt"))), 1));;
    }

    @Test
    @DisplayName("question의 sortOption이 주어지지 않으면 최신순으로 정렬")
    void getQuestionPage() {
        Authentication auth = securityContext.getAuthentication();

        // mocking questionService.writeQuestion

        questionService.writeQuestion("sub", "content");

        // mocking questionService.getQuestionPage(keyword, pageable)
        Pageable questionListPageable = PageRequest.of(0, 5);

        Page<QuestionListDto> questionPage = questionService.getQuestionPage("", questionListPageable);

        Assertions.assertThat(questionPage.getSort()).isEqualTo(Sort.by(Sort.Order.desc("createdAt")));

//        // mocking questionService.questionInfo
//        Mockito.when(repository.findById(1L))
//                .thenReturn(Optional.of(question));
//        PageImpl<Answer> answers = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 5), 0);
//        Mockito.when(answerRepository.findAllByQuestionId(1L, PageRequest.of(0, 5), SortOption.TIME))
//                .thenReturn(answers);
//        PageImpl<Comment> comments = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 5), 0);
//        Mockito.when(commentService.comments(1L, PageRequest.of(0, 5)))
//                .thenReturn(comments);
//
//        Page<QuestionListDto> questionPage = questionService.getQuestionPage("", SortOption.TIME, PageRequest.of(0, 5));
//
//
//        Assertions.assertThat(questionPage).contains()
    }
    @Test
    @DisplayName("question을 id로 검색해 questionDto로 리턴")
    void getQuestionInfo() {

    }

    @Test
    void writeQuestion() {
    }

    @Test
    void modify() {
    }

    @Test
    void delete() {
    }

    @Test
    void recommend() {
    }

    @Test
    void getQuestion() {
    }
}