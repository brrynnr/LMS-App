package com.lms.app.model;

public class Grade {

    private final String gradeId;
    private final double score;
    private final String feedback;

    public Grade(String gradeId, double score, String feedback) {
        this.gradeId = gradeId;
        this.score = score;
        this.feedback = feedback;
    }

    public String getGradeId() { return gradeId; }
    public double getScore() { return score; }
    public String getFeedback() { return feedback; }
}
