package org.example.sbb.app.global;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class UserAuthenticationManager implements AuthenticationManager {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String id = (String) authentication.getPrincipal();
        String credentials = (String) authentication.getCredentials();

        UserDetails user = userDetailsService.loadUserByUsername(id);

        if (!passwordEncoder.matches(credentials, user.getPassword()))
            throw new AccessDeniedException("Invalid username or password");

        return new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
    }
}
