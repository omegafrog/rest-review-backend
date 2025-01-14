package org.example.sbb.app.domain.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserH2Repository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser register(String id, String password1, String password2 , String email){
        if(!password1.equals(password2)) throw new IllegalArgumentException("Passwords do not match");
        return userRepository.save(new SiteUser(id, passwordEncoder.encode(password1), email));
    }

}
