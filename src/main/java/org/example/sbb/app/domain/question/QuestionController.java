package org.example.sbb.app.domain.question;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sbb.app.domain.dto.*;
import org.example.sbb.app.domain.answer.AnswerService;
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
    private final AnswerService answerService;

    @GetMapping
    public String questions(Model model,
                            @RequestParam(name = "keyword", defaultValue = "") String keyword,
                            @PageableDefault(size = 10) Pageable pageable,
                            @ModelAttribute(name="searchForm") SearchForm searchForm) {
        Page<QuestionListDto> all = service.getQuestionPage(keyword, pageable);

        model.addAttribute("paging", all);
        model.addAttribute("keyword", keyword);
        return "question/question_list";
    }


    @GetMapping("/{id}")
    public String question(Model model, @PathVariable Long id, @ModelAttribute(name="form") AnswerForm form, @PageableDefault(size=5) Pageable pageable ) {
        QuestionDto dto = service.getQuestionInfo(id, pageable);

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(bindingResult.hasErrors()) {
            return "question/question_write";
        }
        service.writeQuestion(form.getSubject(), form.getContent(), auth);
        return "redirect:write/confirm";
    }
    @GetMapping("/write/confirm")
    public String writeConfirmPage() {
       return "question/write_confirm";
    }

    @GetMapping("/{id}/modify")
    public String modifyQuestionPage(@ModelAttribute(name="form") QuestionForm form, @PathVariable Long id) {
        QuestionDto dto = service.getQuestionInfo(id, PageRequest.of(0, 10));
        form.setSubject(dto.subject());
        form.setContent(dto.content());
        return "question/question_write";
    }
    @PostMapping("/{id}/modify")
    public String modifyQuestion(@Valid @ModelAttribute(name="form") QuestionForm form, @PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        service.modify(id, form.getSubject(), form.getContent(), auth);
        return "redirect:/sbb/questions/"+id;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/delete")
    public String deleteQuestion( @PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        service.delete(id, auth);
        return "redirect:/sbb/questions";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/recommend")
    public String recommendQuestion( @PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        service.recommend(id, auth);
        return "redirect:/sbb/questions/"+id;
    }
}

