package org.example.sbb.app.domain.question;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/sbb/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService service;

    @GetMapping
    public String questions(Model model) {
        List<Question> all = service.getQuestionList();

        model.addAttribute("list", all);
        return "question_list";
    }
}

