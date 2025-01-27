package org.example.sbb.app.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginUserForm {
    private String id;
    private String password;
}
