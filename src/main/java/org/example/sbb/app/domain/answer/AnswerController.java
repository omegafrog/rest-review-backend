package org.example.sbb.app.domain.answer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sbb.app.domain.dto.AnswerDto;
import org.example.sbb.app.domain.dto.AnswerForm;
import org.example.sbb.app.domain.dto.QuestionDto;
import org.example.sbb.app.domain.question.QuestionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sbb")
@RequiredArgsConstructor
@Slf4j
public class AnswerController {

    private final AnswerService service;
    private final QuestionService questionService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/questions/{question-id}/answers")
    public String writeAnswer(@PathVariable(name="question-id") Long questionId,
                              @Valid @ModelAttribute(name="form") AnswerForm form,
                              BindingResult bindingResult ,Model model) {
        if(bindingResult.hasErrors()) {
            QuestionDto dto = questionService.getQuestionInfo(questionId);
            List<AnswerDto> answerList = service.getAnswerList(questionId);
            questionService.getQuestionInfo(questionId);
            model.addAttribute("question", dto);
            model.addAttribute("answers", answerList);
            return "question/question_info";
        }
        service.writeAnswer(questionId, form.getContent(), SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/sbb/questions/"+questionId;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/questions/{question-id}/answers/{answer-id}/modify")
    public String modifyAnswerPage(@PathVariable(name="question-id") Long questionId, @PathVariable(name="answer-id") Long answerId,
                                   @ModelAttribute(name="form") AnswerForm form,
                                   Model model) {
        log.debug("{}, {}", questionId, answerId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        service.prepareAnswerForm(answerId, form, auth);
        model.addAttribute("questionId", questionId);
        model.addAttribute("answerId", answerId);
        return "answer/answer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/questions/{question-id}/answers/{answer-id}")
    public String modifyingAnswer(@PathVariable(name="question-id") Long questionId, @PathVariable(name="answer-id") Long answerId,
                                  @ModelAttribute(name="form") AnswerForm form){
        log.info("{}, {}", questionId, answerId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        service.modify(answerId, form.getContent(), auth);
        return "redirect:/sbb/questions/"+questionId;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/questions/{question-id}/answers/{answer-id}/delete")
    public String deleteAnswer(@PathVariable(name="question-id") Long questionId, @PathVariable(name="answer-id") Long answerId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        service.delete(answerId, auth);
        return "redirect:/sbb/questions/"+questionId;
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/questions/{question-id}/answers/{answer-id}/recommend")
    public String recommendAnswer(@PathVariable(name="question-id") Long questionId, @PathVariable(name="answer-id") Long answerId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        service.recommend(answerId, auth);
        return "redirect:/sbb/questions/"+questionId;
    }
}
