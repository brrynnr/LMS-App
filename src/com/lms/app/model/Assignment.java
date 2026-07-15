package com.lms.app.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class Assignment {

    private final String assignmentId;
    private String title;
    private LocalDate dueDate;
    private final Course course;

    private final ObservableList<Submission> submissions = FXCollections.observableArrayList();

    public Assignment(String assignmentId, String title, LocalDate dueDate, Course course) {
        this.assignmentId = assignmentId;
        this.title = title;
        this.dueDate = dueDate;
        this.course = course;
    }

    public String getAssignmentId() { return assignmentId; }
    public String getTitle() { return title; }
    public LocalDate getDueDate() { return dueDate; }
    public Course getCourse() { return course; }
    public ObservableList<Submission> getSubmissions() { return submissions; }

    /** Used by the activity diagram's "Deadline Passed?" decision. */
    public boolean isPastDue() {
        return LocalDate.now().isAfter(dueDate);
    }

    @Override
    public String toString() {
        return title;
    }
}
