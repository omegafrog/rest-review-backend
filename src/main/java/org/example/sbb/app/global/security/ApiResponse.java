package org.example.sbb.app.global.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiResponse {
    private String code;
    private String message;
    private Object data;
}
