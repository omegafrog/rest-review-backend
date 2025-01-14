package org.example.sbb.app.domain.answer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sbb.app.domain.dto.AnswerDto;
import org.example.sbb.app.domain.dto.AnswerForm;
import org.example.sbb.app.domain.dto.QuestionDto;
import org.example.sbb.app.domain.question.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sbb")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService service;
    private final QuestionService questionService;
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
        service.writeAnswer(questionId, form.getContent());
        return "redirect:/sbb/questions/"+questionId;
    }
}
