package org.example.sbb.app;

import org.example.sbb.app.domain.user.UserH2Repository;
import org.example.sbb.app.domain.user.UserRole;
import org.example.sbb.app.domain.user.UserSecurityService;
import org.example.sbb.app.global.UserAuthenticationManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain authenticationChain(HttpSecurity http, UserAuthenticationManager authenticationManager) throws Exception {
        http
                .securityMatcher("/sbb/user/**")
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().permitAll());

        http = baseOptions(http);
        return http.build();
    }

    @Bean
    public SecurityFilterChain questionChain(HttpSecurity http, UserAuthenticationManager authenticationManager) throws Exception {
        http
                .authenticationManager(authenticationManager)
                .securityMatcher("/sbb/questions/**")
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/sbb/questions/write")
                                .hasRole("USER")
                                .anyRequest().permitAll());
        http = baseOptions(http);
        return http.build();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }

    private HttpSecurity baseOptions(HttpSecurity http) throws Exception {
        return http
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .formLogin(formLogin ->
                formLogin
                        .loginPage("/sbb/user/login")
                        .loginProcessingUrl("/sbb/user/login")
                        .usernameParameter("id")
                        .passwordParameter("password")
        );
    }
}
