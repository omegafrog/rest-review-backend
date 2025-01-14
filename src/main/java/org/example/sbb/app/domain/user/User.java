package org.example.sbb.app.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class User {
    @Id
    @Column(unique = true)
    private String id;
    private String password;
    @Column(unique = true)
    private String email;
}
