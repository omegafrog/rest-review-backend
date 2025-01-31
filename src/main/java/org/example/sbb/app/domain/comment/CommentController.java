package org.example.sbb.app.domain.comment;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sbb.app.domain.comment.dto.CommentForm;
import org.example.sbb.app.domain.question.question.service.QuestionReadService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sbb")
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final QuestionReadService questionService;

    @PostMapping("/questions/{id}/comments")
    public String writeComment(HttpServletRequest request, @PathVariable(name="id")Long questionId,
                               @ModelAttribute(name = "form") CommentForm form,
                               BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
           return "question/"+questionId+"/comments";
        }
        String targetName = getTargetResourceName(request);

        commentService.writeComment(form, targetName, questionId);
        String[] split = request.getRequestURI().split("/");
        String location = split[0] +"/"+ split[1] +"/"+ split[2] +"/"+ split[3];
        log.debug("location:{}", location);
        return "redirect:"+location;
    }

    @PostMapping("/questions/{question-id}/answers/{answer-id}comments")
    public String writeComment(HttpServletRequest request,@PathVariable(name="question-id")Long questionId,
                               @PathVariable(name="answer-id") Long answerId,
                               @ModelAttribute(name = "form") CommentForm form) {
        String targetName = getTargetResourceName(request);

        commentService.writeComment(form, targetName, answerId);
        String[] split = request.getRequestURI().split("/");
        String location = split[0] +"/"+ split[1] +"/"+ split[2] +"/"+ split[3];
        log.debug("location:{}", location);
        return "redirect:"+location;
    }
    private static String getTargetResourceName(HttpServletRequest request) {
        return request.getRequestURI().split("/")[2];
    }


}
