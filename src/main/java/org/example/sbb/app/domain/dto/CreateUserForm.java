package org.example.sbb.app.domain.dto;

import jakarta.persistence.Temporal;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class CreateUserForm {
    @NotBlank
    private final String id;
    @NotBlank
    @Size(max=100)
    private final String password1;
    @Size(max=100)
    @NotBlank
    private final String password2;
    @NotBlank
    @Email
    private final String email;
}
