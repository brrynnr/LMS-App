package com.lms.app.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Course {

    private final String courseId;
    private String title;
    private String description;
    private Instructor instructor;

    private final ObservableList<Student> enrolledStudents = FXCollections.observableArrayList();
    private final ObservableList<Assignment> assignments = FXCollections.observableArrayList();
    private final ObservableList<Announcement> announcements = FXCollections.observableArrayList();

    public Course(String courseId, String title, String description, Instructor instructor) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.instructor = instructor;
    }

    public String getCourseId() { return courseId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Instructor getInstructor() { return instructor; }
    public void setInstructor(Instructor instructor) { this.instructor = instructor; }

    public ObservableList<Student> getEnrolledStudents() { return enrolledStudents; }
    public ObservableList<Assignment> getAssignments() { return assignments; }
    public ObservableList<Announcement> getAnnouncements() { return announcements; }

    @Override
    public String toString() {
        // Makes the object read nicely inside ComboBox / TableView cells.
        return title;
    }
}
