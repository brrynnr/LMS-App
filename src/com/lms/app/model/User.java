package com.lms.app.model;

import java.io.Serializable;

/**
 * Base class for everyone who can log into the system.
 */
public abstract class User implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String userId;
    protected String name;
    protected String email;
    protected String password;
    protected boolean isActive;

    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isActive = true;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
    public void setName(String name) { this.name = name; }

    public abstract String getRole();
}