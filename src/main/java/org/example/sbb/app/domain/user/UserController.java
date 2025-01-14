package org.example.sbb.app.domain.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sbb.app.domain.dto.CreateUserForm;
import org.example.sbb.app.domain.dto.LoginUserForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sbb/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String registerPage(@ModelAttribute(name="form") CreateUserForm form){
        return "auth/register";
    }
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute(name="form") CreateUserForm form, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
           return "auth/register";
        }
        userService.register(form.getId(), form.getPassword1(), form.getPassword2(), form.getEmail());
        return "auth/success";
    }

    @GetMapping("/login")
    public String loginPage(@ModelAttribute(name = "form") LoginUserForm form,
                            @RequestParam(name = "error", required = false) String error, Model model) {
        if(error!=null)
            model.addAttribute("error", error);
        return "auth/login";
    }
}
