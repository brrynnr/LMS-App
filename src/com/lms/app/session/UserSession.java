package com.lms.app.session;

import java.io.Serializable;

public class UserSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String userId;
    private final String role;

    public UserSession(String userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }
}