package org.example.sbb.app.domain.question.answer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sbb.app.domain.question.answer.dto.AnswerDto;
import org.example.sbb.app.domain.question.answer.dto.AnswerForm;
import org.example.sbb.app.domain.question.answer.dto.AnswerListDto;
import org.example.sbb.app.domain.question.answer.service.AnswerService;
import org.example.sbb.app.global.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sbb/v1")
@RequiredArgsConstructor
@Slf4j
public class AnswerApiControllerV1 {
    private final AnswerService service;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/questions/{question-id}/answers")
    public ApiResponse writeAnswer(@PathVariable(name="question-id") Long questionId,
                                   @Valid @ModelAttribute(name="form") AnswerForm form,
                                   BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, bindingResult);
        }
        AnswerDto answerDto = service.writeAnswer(questionId, form.getContent() );
        return ApiResponse.ok("answer 작성 성공.", answerDto);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/questions/{question-id}/answers/{answer-id}")
    public ApiResponse modifyingAnswer( @PathVariable(name="answer-id") Long answerId,
                                        @ModelAttribute(name="form") AnswerForm form){
        AnswerDto answerDto = service.modify(answerId, form.getContent());
        return ApiResponse.ok("답변 수정 성공. ", answerDto);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/questions/{question-id}/answers/{answer-id}")
    public ApiResponse deleteAnswer(@PathVariable(name="answer-id") Long answerId, @PathVariable("question-id") String parameter){
        service.delete(answerId);
        return ApiResponse.ok("답변 삭제 성공.", null);
    }

    // TODO : answer의 recommend를 추가할 때 patch mapping으로 해서 modify와 같은 resource path를 가지고 queryParameter로 다르게 처리하도록 해야 한다.
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/questions/{question-id}/answers/{answer-id}")
    public ApiResponse recommendAnswer(@PathVariable(name="question-id") Long questionId, @PathVariable(name="answer-id") Long answerId){
        AnswerDto answerDto = service.recommend(answerId);
        return ApiResponse.ok("답변 추천 성공.", answerDto);
    }

    @GetMapping("/answers")
    public ApiResponse recentAnswers(@PageableDefault(page = 0, size = 5) Pageable pageable){
        Page<AnswerListDto> answerPage = service.getAnswerPage(pageable);
        return ApiResponse.ok("최근 답변 조회 성공", answerPage);
    }
}
