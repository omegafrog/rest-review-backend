package org.example.sbb.app;

import org.example.sbb.app.global.security.Oauth2AuthenticationSuccessHandler;
import org.example.sbb.app.global.security.UserAuthenticationManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain authenticationChain(HttpSecurity http, OAuth2UserService siteUserOidcUserService ,
                                                   Oauth2AuthenticationSuccessHandler oauth2SuccessHandler) throws Exception {
        http
                .securityMatcher("/sbb/user/**")
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().permitAll());

        http = baseOptions(http, siteUserOidcUserService, oauth2SuccessHandler);
        return http.build();
    }
    @Bean
    public SecurityFilterChain oauth2Chain(HttpSecurity http, OAuth2UserService siteUserOidcUserService
    ,Oauth2AuthenticationSuccessHandler oauth2SuccessHandler) throws Exception {
        http
                .securityMatcher("/oauth2/**", "/login/oauth2/**")
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().permitAll());
        http = baseOptions(http, siteUserOidcUserService, oauth2SuccessHandler);
        return http.build();
    }

    @Bean
    public SecurityFilterChain questionChain(HttpSecurity http, UserAuthenticationManager authenticationManager,
                                             OAuth2UserService siteUserOidcUserService,
                                             Oauth2AuthenticationSuccessHandler oauth2SuccessHandler) throws Exception {
        http
                .authenticationManager(authenticationManager)
                .securityMatcher("/sbb/questions/**")
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/sbb/questions/write")
                                .hasRole("USER")
                                .anyRequest().permitAll());
        http = baseOptions(http, siteUserOidcUserService, oauth2SuccessHandler);
        return http.build();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }

    private HttpSecurity baseOptions(HttpSecurity http, OAuth2UserService siteUserOidcUserService,
                                     AuthenticationSuccessHandler authenticationSuccessHandler) throws Exception {
        return http
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/sbb/user/login")
                                .loginProcessingUrl("/sbb/user/login")
                                .usernameParameter("id")
                                .passwordParameter("password"))
                .logout(
                        logout ->
                                logout.logoutRequestMatcher(new AntPathRequestMatcher("/sbb/user/logout"))
                                        .logoutSuccessUrl("/sbb/questions")
                                        .invalidateHttpSession(true)
                )
                .oauth2Login(oauth2->oauth2
                        .loginPage("/sbb/user/google/login")
                        .userInfoEndpoint(userInfo->
                                userInfo.oidcUserService(siteUserOidcUserService))
                        .successHandler(authenticationSuccessHandler));

    }
}
