package org.example.sbb.app.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {
    private final UserH2Repository userH2Repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SiteUser siteUser = userH2Repository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        List<GrantedAuthority> authorityList = new ArrayList<>();
        if("admin".equals(username))
            authorityList.add(new SimpleGrantedAuthority(UserRole.ADMIN.value));
        else
            authorityList.add(new SimpleGrantedAuthority(UserRole.USER.value));
        return new User(username, siteUser.getPassword(), authorityList);
    }
}
