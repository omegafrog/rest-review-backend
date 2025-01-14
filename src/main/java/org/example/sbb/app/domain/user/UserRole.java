package org.example.sbb.app.domain.user;

public enum UserRole {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN");
    final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

