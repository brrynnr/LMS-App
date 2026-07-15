package com.lms.app.model;

import java.time.LocalDate;

public class Submission {

    private final String submissionId;
    private final Student student;
    private final Assignment assignment;
    private final String fileName;
    private final LocalDate submittedDate;
    private Grade grade;

    public Submission(String submissionId, Student student, Assignment assignment, String fileName) {
        this.submissionId = submissionId;
        this.student = student;
        this.assignment = assignment;
        this.fileName = fileName;
        this.submittedDate = LocalDate.now();
    }

    public String getSubmissionId() { return submissionId; }
    public Student getStudent() { return student; }
    public Assignment getAssignment() { return assignment; }
    public String getFileName() { return fileName; }
    public LocalDate getSubmittedDate() { return submittedDate; }
    public Grade getGrade() { return grade; }
    public void setGrade(Grade grade) { this.grade = grade; }
}
