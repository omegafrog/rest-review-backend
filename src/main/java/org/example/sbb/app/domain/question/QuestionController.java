package org.example.sbb.app.domain.question;


import lombok.RequiredArgsConstructor;
import org.example.sbb.app.domain.dto.AnswerDto;
import org.example.sbb.app.domain.dto.QuestionDto;
import org.example.sbb.app.domain.answer.AnswerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/write")
    public String writeQuestionPage() {
        return "question/question_write";
    }

    @PostMapping("/write")
    public String writeQuestion(@RequestParam(name = "subject") String subject, @RequestParam(name = "content") String content) {
        service.writeQuestion(subject, content);
        return "redirect:write/confirm";
    }
    @GetMapping("/write/confirm")
    public String writeConfirmPage() {
       return "question/write_confirm";
    }
}

