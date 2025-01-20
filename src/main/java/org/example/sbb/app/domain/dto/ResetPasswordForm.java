package org.example.sbb.app.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class ResetPasswordForm {
    private String password1;
    private String password2;

    public boolean match(){
        return password1.equals(password2);
    }
}
