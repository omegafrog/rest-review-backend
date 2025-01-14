package org.example.sbb.app.domain.question;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sbb.app.domain.dto.AnswerDto;
import org.example.sbb.app.domain.dto.AnswerForm;
import org.example.sbb.app.domain.dto.QuestionDto;
import org.example.sbb.app.domain.answer.AnswerService;
import org.example.sbb.app.domain.dto.QuestionForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/sbb/questions")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {
    private final QuestionService service;
    private final AnswerService answerService;

    @GetMapping
    public String questions(Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<Question> all = service.getQuestionPage(pageable);

        model.addAttribute("paging", all);
        return "question/question_list";
    }

    @GetMapping("/{id}")
    public String question(Model model, @PathVariable Long id, @ModelAttribute(name="form") AnswerForm form ) {
        QuestionDto dto = service.getQuestionInfo(id);
        List<AnswerDto> all = answerService.getAnswerList(id);

        model.addAttribute("question", dto);
        model.addAttribute("answers", all);

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
        QuestionDto dto = service.getQuestionInfo(id);
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
}

