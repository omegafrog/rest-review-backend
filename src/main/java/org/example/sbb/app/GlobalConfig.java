package org.example.sbb.app;

import org.example.sbb.app.domain.user.UserH2Repository;
import org.example.sbb.app.domain.user.UserService;
import org.example.sbb.app.global.security.Oauth2AuthenticationSuccessHandler;
import org.example.sbb.app.global.security.SiteUserOidcUserService;
import org.example.sbb.app.global.security.UserAuthenticationManager;
import org.example.sbb.app.global.security.UserSecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;

@Configuration
@EnableJpaAuditing
public class GlobalConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserAuthenticationManager userAuthenticationManager(UserH2Repository userH2Repository){
        return new UserAuthenticationManager(userSecurityService(userH2Repository), passwordEncoder());
    }
    @Bean
    public UserDetailsService userSecurityService(UserH2Repository userH2Repository){
        return new UserSecurityService(userH2Repository);
    }
    @Bean
    public OAuth2UserService siteUserOidcUserService(){
        return new SiteUserOidcUserService( );
    }
    @Bean
    public Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler(UserService userService){
        return new Oauth2AuthenticationSuccessHandler(userService);
    }
}
