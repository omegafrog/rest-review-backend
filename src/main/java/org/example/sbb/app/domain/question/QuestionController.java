package org.example.sbb.app.domain.question;


import lombok.RequiredArgsConstructor;
import org.example.sbb.app.AnswerDto;
import org.example.sbb.app.QuestionDto;
import org.example.sbb.app.domain.answer.AnswerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/sbb/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService service;
    private final AnswerService answerService;

    @GetMapping
    public String questions(Model model) {
        List<Question> all = service.getQuestionList();

        model.addAttribute("list", all);
        return "question/question_list";
    }

    @GetMapping("/{id}")
    public String question(Model model, @PathVariable Long id) {
        QuestionDto dto = service.getQuestionInfo(id);
        List<AnswerDto> all = answerService.getAnswerList(id);
        model.addAttribute("question", dto);
        model.addAttribute("answers", all);
        return "question/question_info";
    }
}

