package org.example.sbb.app.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Slf4j
public class ApiResponse {
    private HttpStatus code;
    private String message;
    private Object data;

    public static  ApiResponse of(HttpStatus code, String message, Object data) {
        return new ApiResponse(code, message, data);
    }

    public static ApiResponse of(HttpStatus code, Exception e){
        log.debug("{}:{}", code, e.getMessage());
        return new ApiResponse(code, e.getMessage(), null);
    }

    public static ApiResponse of(HttpStatus code, BindingResult bindingResult){
        log.debug("{}:{}", code, bindingResult);
        String message = bindingResult.getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + " : " + fe.getCode() + " : "  + fe.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining("\n"));
        return new ApiResponse(code, message, null);
    }
    public static ApiResponse ok(String message, Object data) {
        return ApiResponse.of(HttpStatus.OK, message, data);
    }
}
