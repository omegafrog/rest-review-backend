package org.example.sbb.app.domain.user;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.sbb.app.domain.dto.ResetPasswordForm;
import org.example.sbb.app.domain.dto.SiteUserInfoDto;
import org.example.sbb.app.global.RecoveryCache;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserH2Repository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RecoveryCache cache = RecoveryCache.getCache();
    private final JavaMailSender mailSender;

    public SiteUser register(String id, String password1, String password2 , String email){
        if(!password1.equals(password2)) throw new IllegalArgumentException("Passwords do not match");
        return userRepository.save(new SiteUser(id, passwordEncoder.encode(password1), email));
    }

    public SiteUser findUser(String username) {
        return userRepository.findById(username)
                .orElseThrow(EntityNotFoundException::new);
    }

    public void recovery(String userId) {
        Optional<SiteUser> byId = userRepository.findById(userId);
        if(byId.isEmpty())
            throw new IllegalArgumentException("User not found");
        String recoveryResourcePath = createRecoveryResourcePath();
        String path = "/sbb/user/reset?key="+recoveryResourcePath;
        cache.put(recoveryResourcePath, userId );

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(byId.get().getEmail());
            helper.setSubject("Recovery resource");
            helper.setText("""
                    이 url으로 들어가 다시 로그인해주세요.
                    <a href=%s>
                    %s</a>""".formatted(path, path));
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String createRecoveryResourcePath() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(20);

        for (int i = 0; i < 20; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }

        return sb.toString();
    }

    public void reset(String recoveryKey, ResetPasswordForm form) {
        String userId = RecoveryCache.get(recoveryKey);
        if(userId == null)
            throw new IllegalArgumentException("User not found");
        SiteUser siteUser = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        if(!form.match())
            throw new IllegalArgumentException("비밀번호 재확인 실패.");
        String encoded = passwordEncoder.encode(form.getPassword1());
        siteUser.resetPassword(encoded );
    }

    public void reset( ResetPasswordForm form) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if( auth !=null && auth.isAuthenticated() && auth.getPrincipal() instanceof User user){
            String userId = user.getUsername();
            SiteUser siteUser = userRepository.findById(userId)
                    .orElseThrow(EntityNotFoundException::new);

            if(!form.match())
                throw new IllegalArgumentException("비밀번호 재확인 실패.");
            String encoded = passwordEncoder.encode(form.getPassword1());
            siteUser.resetPassword(encoded );
        }
        else
            throw new IllegalArgumentException("User not found");
    }

    public SiteUserInfoDto getUserInfo() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String id= user.getUsername();
        SiteUser siteUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return SiteUserInfoDto.of(siteUser);
    }
}
