package org.example.sbb.app.domain.question.answer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sbb.app.domain.question.answer.dto.AnswerDto;
import org.example.sbb.app.domain.question.answer.dto.AnswerForm;
import org.example.sbb.app.domain.question.answer.dto.AnswerListDto;
import org.example.sbb.app.domain.question.question.dto.QuestionDto;
import org.example.sbb.app.domain.question.question.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
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
            QuestionDto dto = questionService.getQuestionInfo(questionId, PageRequest.of(0, 10), PageRequest.of(0, 10));
            List<AnswerDto> answerList = service.getAnswerList(questionId);
            model.addAttribute("question", dto);
            model.addAttribute("answers", answerList);
            return "question/question_info";
        }
        AnswerDto answerDto = service.writeAnswer(questionId, form.getContent() );
        return "redirect:/sbb/questions/"+questionId+"#answer_"+answerDto.id();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/questions/{question-id}/answers/{answer-id}/modify")
    public String modifyAnswerPage(@PathVariable(name="question-id") Long questionId, @PathVariable(name="answer-id") Long answerId,
                                   @ModelAttribute(name="form") AnswerForm form,
                                   Model model) {
        log.debug("{}, {}", questionId, answerId);
        service.prepareAnswerForm(answerId, form);
        model.addAttribute("questionId", questionId);
        model.addAttribute("answerId", answerId);
        return "answer/answer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/questions/{question-id}/answers/{answer-id}")
    public String modifyingAnswer(@PathVariable(name="question-id") Long questionId, @PathVariable(name="answer-id") Long answerId,
                                  @ModelAttribute(name="form") AnswerForm form){
        log.info("{}, {}", questionId, answerId);
        AnswerDto answerDto = service.modify(answerId, form.getContent());
        return "redirect:/sbb/questions/"+questionId+"#answer_"+answerDto.id();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/questions/{question-id}/answers/{answer-id}/delete")
    public String deleteAnswer(@PathVariable(name="question-id") Long questionId, @PathVariable(name="answer-id") Long answerId){
        service.delete(answerId);
        return "redirect:/sbb/questions/"+questionId;
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/questions/{question-id}/answers/{answer-id}/recommend")
    public String recommendAnswer(@PathVariable(name="question-id") Long questionId, @PathVariable(name="answer-id") Long answerId){
        AnswerDto answerDto = service.recommend(answerId);
        return "redirect:/sbb/questions/"+questionId+"#answer_"+answerDto.id();
    }

    @GetMapping("/answers")
    public String recentAnswers(Model model,@PageableDefault(page = 0, size = 5) Pageable pageable){
        Page<AnswerListDto> answerPage = service.getAnswerPage(pageable);
        model.addAttribute("answers", answerPage);
        return "answer/answer_list";
    }

}
