package org.example.sbb.app.domain.comment;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private String comments(@PathVariable Long questionId,
                            @PageableDefault(page = 1, size = 5) Pageable pageable,
                            @RequestParam(name="sortOption") String sortOption,
                            Model model) {
        return null;
    }

}
