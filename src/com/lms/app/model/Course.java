package com.lms.app.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Course {

    private final String courseId;
    private String title;
    private String description;
    private Instructor instructor;
    private String program;
    private String yearLevel;
    private boolean isPrerequisite;

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

    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }

    public String getYearLevel() { return yearLevel; }
    public void setYearLevel(String yearLevel) { this.yearLevel = yearLevel; }

    public boolean isPrerequisite() { return isPrerequisite; }
    public void setPrerequisite(boolean prerequisite) { isPrerequisite = prerequisite; }

    public ObservableList<Student> getEnrolledStudents() { return enrolledStudents; }
    public ObservableList<Assignment> getAssignments() { return assignments; }
    public ObservableList<Announcement> getAnnouncements() { return announcements; }

    @Override
    public String toString() {
        return title;
    }
}
