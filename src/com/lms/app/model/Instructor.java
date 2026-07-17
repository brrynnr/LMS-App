package com.lms.app.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Instructor extends User {

    private String department;
    private String employeeId;
    private String designation;
    private final ObservableList<Course> coursesTaught = FXCollections.observableArrayList();

    public Instructor(String userId, String name, String email, String password) {
        super(userId, name, email, password);
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public ObservableList<Course> getCoursesTaught() {
        return coursesTaught;
    }

    @Override
    public String getRole() {
        return "Instructor";
    }
}
