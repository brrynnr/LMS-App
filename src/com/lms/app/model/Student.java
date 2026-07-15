package com.lms.app.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Student extends User {

    private final ObservableList<Course> enrolledCourses = FXCollections.observableArrayList();

    public Student(String userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    public ObservableList<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    /** Enrolls this student in a course and keeps both sides of the relationship in sync. */
    public void enroll(Course course) {
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
            course.getEnrolledStudents().add(this);
        }
    }

    @Override
    public String getRole() {
        return "Student";
    }
}
