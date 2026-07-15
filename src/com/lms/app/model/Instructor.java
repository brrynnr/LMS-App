package com.lms.app.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Instructor extends User {

    private final ObservableList<Course> coursesTaught = FXCollections.observableArrayList();

    public Instructor(String userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    public ObservableList<Course> getCoursesTaught() {
        return coursesTaught;
    }

    @Override
    public String getRole() {
        return "Instructor";
    }
}
