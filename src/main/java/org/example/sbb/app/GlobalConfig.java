package org.example.sbb.app;

import org.example.sbb.app.domain.user.UserH2Repository;
import org.example.sbb.app.domain.user.UserSecurityService;
import org.example.sbb.app.global.UserAuthenticationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
}
