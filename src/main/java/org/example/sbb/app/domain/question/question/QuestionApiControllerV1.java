package org.example.sbb.app.domain.question.question;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sbb.app.domain.question.answer.SortOption;
import org.example.sbb.app.domain.question.question.dto.QuestionDto;
import org.example.sbb.app.domain.question.question.dto.QuestionForm;
import org.example.sbb.app.global.security.ApiResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/sbb/v1/questions")
@RequiredArgsConstructor
@Slf4j
public class QuestionApiControllerV1 {
    private final QuestionService service;

    @GetMapping
    public ApiResponse questions(@RequestParam(name = "keyword", defaultValue = "") String keyword,
                            @RequestParam(name="sortOption", defaultValue = "time") String sortOption,
                            @PageableDefault(size = 10 ) Pageable pageable) {
        return new ApiResponse(
                HttpStatus.OK.toString(),
                "get questions success.",
                service.getQuestionPage(keyword, SortOption.of(sortOption), pageable));
    }


    @GetMapping("/{id}")
    public ApiResponse question(@PathVariable Long id,
                           @RequestParam(name="answer-page", defaultValue = "0") int answerPage,
                           @RequestParam(name="answer-size", defaultValue = "5") int answerSize,
                           @RequestParam(name="comment-page", defaultValue = "0") int commentPage,
                           @RequestParam(name="comment-size", defaultValue = "5") int commentSize,
                           @RequestParam(name="sortOption", defaultValue = "time") String option) {

        Pageable answerPageable = PageRequest.of(answerPage, answerSize);
        Pageable commentPageable = PageRequest.of(commentPage, commentSize);
        QuestionDto dto = service.getQuestionInfo(id, SortOption.of(option), answerPageable, commentPageable);

        return new ApiResponse(HttpStatus.OK.toString(), "get question success.", dto);
    }

//    @GetMapping("/write")
//    public String writeQuestionPage(@RequestBody QuestionForm form) {
//        return "question/question_write";
//    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ApiResponse writeQuestion(@Valid @RequestBody QuestionForm form, BindingResult bindingResult,
                                     HttpServletResponse response) {
        Authentication auth = getAuthentication();
        if(bindingResult.hasErrors()) {
            return new ApiResponse(HttpStatus.BAD_REQUEST.toString(), "write question failed.",bindingResult);
        }
        service.writeQuestion(form.getSubject(), form.getContent(), auth);
        return new ApiResponse(HttpStatus.OK.toString(),"write question success.", "write question successful");
    }

    private static Authentication getAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth;
    }

    @GetMapping("/write/confirm")
    public String writeConfirmPage() {
       return "question/write_confirm";
    }

    @GetMapping("/{id}/modify")
    public String modifyQuestionPage(@ModelAttribute(name="form") QuestionForm form, @PathVariable Long id) {
        QuestionDto dto = service.getQuestionInfo(id, PageRequest.of(0, 10), PageRequest.of(0, 5));
        form.setSubject(dto.subject());
        form.setContent(dto.content());
        return "question/question_write";
    }
    @PostMapping("/{id}/modify")
    public String modifyQuestion(@Valid @ModelAttribute(name="form") QuestionForm form, @PathVariable Long id) {
        Authentication auth = getAuthentication();
        service.modify(id, form.getSubject(), form.getContent(), auth);
        return "redirect:/sbb/questions/"+id;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/delete")
    public String deleteQuestion( @PathVariable Long id) {
        Authentication auth = getAuthentication();
        service.delete(id, auth);
        return "redirect:/sbb/questions";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/recommend")
    public String recommendQuestion( @PathVariable Long id) {
        Authentication auth = getAuthentication();
        service.recommend(id, auth);
        return "redirect:/sbb/questions/"+id;
    }
}

