package org.example.sbb.app.global.security;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.sbb.app.domain.user.SiteUser;
import org.example.sbb.app.domain.user.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class Oauth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User principal = token.getPrincipal();
        String email = principal.getAttribute("email");

        try {
            SiteUser user = userService.findUserById(email);

            UsernamePasswordAuthenticationToken roleUser =
                    new UsernamePasswordAuthenticationToken(new User(user.getId(), "", List.of(new SimpleGrantedAuthority("ROLE_USER"))), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
            SecurityContextHolder.getContext().setAuthentication(roleUser);
            getRedirectStrategy().sendRedirect(request, response, "/sbb/questions");
        } catch (EntityNotFoundException e) {
            if(userService.exist(email)){
                getRedirectStrategy().sendRedirect(request, response, "/sbb/user/login?error=already_exists email");
                return;
            }
            userService.register(email, "", "", email);
        }
    }
}
