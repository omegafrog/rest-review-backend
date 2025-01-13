package org.example.sbb.app.domain.answer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sbb")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService service;
    @PostMapping("/questions/{question-id}/answers")
    public String writeAnswer(@PathVariable(name="question-id") Long questionId,
                              @RequestParam(name = "content")String content){
        service.writeAnswer(questionId, content);
        return "redirect:/sbb/questions/"+questionId;
    }
}
