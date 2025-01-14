package org.example.sbb.app.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserH2Repository extends JpaRepository<SiteUser, String> {

}
