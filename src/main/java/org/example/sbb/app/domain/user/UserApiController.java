package org.example.sbb.app.domain.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.sbb.app.domain.user.dto.CreateUserForm;
import org.example.sbb.app.domain.user.dto.RecoveryForm;
import org.example.sbb.app.domain.user.dto.ResetPasswordForm;
import org.example.sbb.app.domain.user.dto.SiteUserInfoDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sbb/v1/user")
@RequiredArgsConstructor
public class UserApiController {

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


    @GetMapping("/recovery")
    public String recoveryPasswordPage(@ModelAttribute(name="form") RecoveryForm form,
                                       @RequestParam(defaultValue = "") String mode) {
        if(mode.equals("confirm"))
            return "auth/recovery_confirm";
        return "auth/recovery";
    }

    @PostMapping("/recovery")
    public String recoveryPassword(@ModelAttribute(name="userId") String userId){
        userService.recovery(userId);
        return "redirect:/sbb/user/recovery?confirm";
    }

    @GetMapping("/reset")
    public String resetPasswordPage(Model model, @ModelAttribute(name="form") ResetPasswordForm form,
                                    @RequestParam(name="key", defaultValue = "") String recoveryKey){
        model.addAttribute("key", recoveryKey);
        return "auth/reset";
    }


    @PostMapping("/reset")
    public String resetPassword(@RequestParam(name="key", required = false) String recoveryKey,
                                @ModelAttribute(name="form") ResetPasswordForm form) {
        if(recoveryKey.isEmpty()){
            userService.reset(form);
        }
        else
            userService.reset(recoveryKey, form);
        return "auth/reset-success";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public String userInfo(Model model) {
        SiteUserInfoDto userInfo = userService.getUserInfo();
        model.addAttribute("user", userInfo);
        return "auth/user-info";
    }

    @GetMapping("/google/login")
    public String googleLogin(){
        return "auth/google-login";
    }

}
