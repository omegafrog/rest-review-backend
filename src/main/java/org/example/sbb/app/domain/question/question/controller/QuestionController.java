package org.example.sbb.app.domain.question.question.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sbb.app.domain.question.answer.SortOption;
import org.example.sbb.app.domain.question.answer.dto.AnswerForm;
import org.example.sbb.app.domain.question.question.QuestionService;
import org.example.sbb.app.domain.question.question.dto.QuestionDto;
import org.example.sbb.app.domain.question.question.dto.QuestionForm;
import org.example.sbb.app.domain.question.question.dto.QuestionListDto;
import org.example.sbb.app.domain.question.question.dto.SearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/sbb/questions")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {
    private final QuestionService service;

    @GetMapping
    public String questions(Model model,
                            @RequestParam(name = "keyword", defaultValue = "") String keyword,
                            @RequestParam(name="sortOption", defaultValue = "time") String sortOption,
                            @PageableDefault(size = 10 ) Pageable pageable,
                            @ModelAttribute(name="searchForm") SearchForm searchForm) {
        Page<QuestionListDto> all = service.getQuestionPage(keyword, SortOption.of(sortOption), pageable);

        model.addAttribute("paging", all);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortOption", sortOption);
        return "question/question_list";
    }


    @GetMapping("/{id}")
    public String question(Model model,
                           @PathVariable Long id,
                           @ModelAttribute(name="form") AnswerForm form,
                           @RequestParam(name="answer-page", defaultValue = "0") int answerPage,
                           @RequestParam(name="answer-size", defaultValue = "5") int answerSize,
                           @RequestParam(name="comment-page", defaultValue = "0") int commentPage,
                           @RequestParam(name="comment-size", defaultValue = "5") int commentSize,
                           @RequestParam(name="sortOption", defaultValue = "time") String option) {

        Pageable answerPageable = PageRequest.of(answerPage, answerSize);
        Pageable commentPageable = PageRequest.of(commentPage, commentSize);
        QuestionDto dto = service.getQuestionInfo(id, SortOption.of(option), answerPageable, commentPageable);

        model.addAttribute("question", dto);
        return "question/question_info";
    }

    @GetMapping("/write")
    public String writeQuestionPage(@ModelAttribute(name="form") QuestionForm form) {
        return "question/question_write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String writeQuestion(@Valid @ModelAttribute(name="form") QuestionForm form, BindingResult bindingResult) {
        Authentication auth = getAuthentication();
        if(bindingResult.hasErrors()) {
            return "question/question_write";
        }
        service.writeQuestion(form.getSubject(), form.getContent());
        return "redirect:write/confirm";
    }

    private static Authentication getAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth;
    }

    @GetMapping("/write/confirm")
    public String writeConfirmPage() {
       return "question/write_confirm";
    }

    @GetMapping("/{id}/modify")
    public String modifyQuestionPage(@ModelAttribute(name="form") QuestionForm form, @PathVariable Long id) {
        QuestionDto dto = service.getQuestionInfo(id, PageRequest.of(0, 10), PageRequest.of(0, 5));
        form.setSubject(dto.subject());
        form.setContent(dto.content());
        return "question/question_write";
    }
    @PostMapping("/{id}/modify")
    public String modifyQuestion(@Valid @ModelAttribute(name="form") QuestionForm form, @PathVariable Long id) {
        Authentication auth = getAuthentication();
        service.modify(id, form.getSubject(), form.getContent());
        return "redirect:/sbb/questions/"+id;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/delete")
    public String deleteQuestion( @PathVariable Long id) {
        Authentication auth = getAuthentication();
        service.delete(id);
        return "redirect:/sbb/questions";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/recommend")
    public String recommendQuestion( @PathVariable Long id) {
        Authentication auth = getAuthentication();
        service.recommend(id);
        return "redirect:/sbb/questions/"+id;
    }
}

