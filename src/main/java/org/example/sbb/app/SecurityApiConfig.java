package org.example.sbb.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.sbb.app.global.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityApiConfig {

    @Bean
    public SecurityFilterChain apiChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/sbb/v1/**");
        baseOptions(http);
        return http.build();
    }

    private HttpSecurity baseOptions(HttpSecurity http) throws Exception {
        return http
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin ->
                        formLogin
                                .loginProcessingUrl("/sbb/v1/user/login")
                                .successHandler(new AuthenticationSuccessHandler() {
                                    @Override
                                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                        response.setContentType("application/json");
                                        response.getWriter().write(
                                                new ObjectMapper()
                                                        .writeValueAsString(new ApiResponse(HttpStatus.OK, "login success", null)));
                                    }
                                })
                                .usernameParameter("id")
                                .passwordParameter("password"))
                .logout(
                        logout ->
                                logout.logoutRequestMatcher(new AntPathRequestMatcher("/sbb/v1/user/logout"))
                                        .invalidateHttpSession(true)
                )
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            if (authException instanceof AuthenticationException) {
                                response.getWriter().write(
                                        new ObjectMapper().writeValueAsString(
                                                new ApiResponse(HttpStatus.FORBIDDEN, "올바르지 않은 권한입니다.", null)));
                            } else {
                                response.getWriter().write(
                                        new ObjectMapper().writeValueAsString(
                                                new ApiResponse(HttpStatus.UNAUTHORIZED, "Authentication required", null)));
                            }
                        }));
    }
}
