package org.example.sbb.app.domain.comment.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sbb.app.domain.comment.dto.CommentForm;
import org.example.sbb.app.domain.comment.service.CommentService;
import org.example.sbb.app.global.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sbb/v1")
@Slf4j
public class CommentApiControllerV1 {

    private final CommentService commentService;

    @PostMapping("/questions/{id}/comments")
    public ApiResponse writeComment(HttpServletRequest request, @PathVariable(name="id")Long questionId,
                                    @ModelAttribute(name = "form") CommentForm form,
                                    BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, bindingResult);
        }
        String targetName = getTargetResourceName(request);

        commentService.writeComment(form, targetName, questionId);

        return ApiResponse.ok("댓글 작성 성공.", null);
    }

    @PostMapping("/questions/{question-id}/answers/{answer-id}/comments")
    public ApiResponse writeComment(HttpServletRequest request,@PathVariable(name="question-id")Long questionId,
                               @PathVariable(name="answer-id") Long answerId,
                               @ModelAttribute(name = "form") CommentForm form) {

        String targetName = getTargetResourceName(request);

        commentService.writeComment(form, targetName, answerId);

        return ApiResponse.ok("댓글 작성 성공.", null);
    }
    private static String getTargetResourceName(HttpServletRequest request) {
        return request.getRequestURI().split("/")[2];
    }


}
