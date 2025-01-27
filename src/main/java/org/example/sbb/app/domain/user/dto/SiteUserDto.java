package org.example.sbb.app.domain.user.dto;

import org.example.sbb.app.domain.user.SiteUser;

public record SiteUserDto(String id, String email) {

    public static SiteUserDto of(SiteUser author) {
        return new SiteUserDto(author.getId(), author.getEmail());
    }
}
