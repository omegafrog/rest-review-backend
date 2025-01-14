package org.example.sbb.app.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name="MEMBER")
@NoArgsConstructor
public class SiteUser {
    @Id
    @Column(unique = true)
    private String id;
    private String password;
    @Column(unique = true)
    private String email;

    public SiteUser(String id, String password, String email) {
        this.id = id;
        this.password = password;
        this.email = email;
    }
}
