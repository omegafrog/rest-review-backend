package org.example.sbb.app.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserH2Repository extends JpaRepository<SiteUser, String> {

    Optional<SiteUser> findByEmail(String email);

    boolean existsByEmail(String email);
}
