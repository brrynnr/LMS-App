package com.lms.app.model;

/**
 * Base class for everyone who can log into the system.
 * Student, Instructor, and Admin all extend this class (matches the class diagram).
 */
public abstract class User {

    protected String userId;
    protected String name;
    protected String email;
    protected String password;

    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public void setName(String name) { this.name = name; }

    /** Returns a human-readable role label, used for display in the UI. */
    public abstract String getRole();
}
