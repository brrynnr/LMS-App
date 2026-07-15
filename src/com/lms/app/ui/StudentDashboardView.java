package com.lms.app.ui;

import com.lms.app.Main;
import com.lms.app.data.DataStore;
import com.lms.app.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class StudentDashboardView {

    public static Scene createScene(Student student) {
        BorderPane root = new BorderPane();
        root.setTop(buildHeader(student));
        root.setCenter(buildTabs(student));
        root.getStyleClass().add("root-pane");

        Scene scene = new Scene(root, 900, 650);
        Styles.apply(scene);
        return scene;
    }

    private static HBox buildHeader(Student student) {
        Label welcome = new Label("Welcome, " + student.getName() + " (Student)");
        welcome.getStyleClass().add("header-label");

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> Main.getPrimaryStage().setScene(LoginView.createScene()));

        HBox header = new HBox(welcome, spacer(), logoutButton);
        header.setPadding(new Insets(15));
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("header-bar");
        return header;
    }

    private static TabPane buildTabs(Student student) {
        TabPane tabs = new TabPane();
        tabs.getTabs().addAll(
                new Tab("My Courses", buildCoursesTab(student)),
                new Tab("Assignments", buildAssignmentsTab(student)),
                new Tab("Grades", buildGradesTab(student)),
                new Tab("Announcements", buildAnnouncementsTab(student))
        );
        tabs.getTabs().forEach(t -> t.setClosable(false));
        return tabs;
    }

    // --- My Courses tab: browse all courses, enroll in one ---
    private static VBox buildCoursesTab(Student student) {
        Label enrolledLabel = new Label("Enrolled Courses");
        TableView<Course> enrolledTable = new TableView<>(student.getEnrolledCourses());
        enrolledTable.getColumns().addAll(courseColumns());

        Label availableLabel = new Label("Available Courses");
        ObservableList<Course> available = FXCollections.observableArrayList(DataStore.getInstance().getCourses());
        TableView<Course> availableTable = new TableView<>(available);
        availableTable.getColumns().addAll(courseColumns());

        Button enrollButton = new Button("Enroll in Selected Course");
        enrollButton.getStyleClass().add("primary-button");
        enrollButton.setOnAction(e -> {
            Course selected = availableTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Select a course first.");
                return;
            }
            if (student.getEnrolledCourses().contains(selected)) {
                showAlert("You are already enrolled in this course.");
                return;
            }
            student.enroll(selected);
            enrolledTable.refresh();
            showAlert("Enrolled in " + selected.getTitle() + ".");
        });

        VBox box = new VBox(10, enrolledLabel, enrolledTable, availableLabel, availableTable, enrollButton);
        box.setPadding(new Insets(15));
        return box;
    }

    // --- Assignments tab: view assignments from enrolled courses, submit one ---
    private static VBox buildAssignmentsTab(Student student) {
        ObservableList<Assignment> myAssignments = FXCollections.observableArrayList();
        for (Course c : student.getEnrolledCourses()) {
            myAssignments.addAll(c.getAssignments());
        }

        TableView<Assignment> table = new TableView<>(myAssignments);
        TableColumn<Assignment, String> titleCol = new TableColumn<>("Assignment");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Assignment, String> dueCol = new TableColumn<>("Due Date");
        dueCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        table.getColumns().addAll(titleCol, dueCol);

        TextField fileField = new TextField();
        fileField.setPromptText("File name (e.g. assignment1.pdf)");

        Button submitButton = new Button("Submit Assignment");
        submitButton.getStyleClass().add("primary-button");
        submitButton.setOnAction(e -> {
            Assignment selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Select an assignment first.");
                return;
            }
            if (selected.isPastDue()) {
                showAlert("Deadline has passed. Submission not allowed.");
                return;
            }
            if (fileField.getText().isBlank()) {
                showAlert("Enter a file name to submit.");
                return;
            }
            String id = DataStore.getInstance().generateId("SUB");
            Submission submission = new Submission(id, student, selected, fileField.getText().trim());
            selected.getSubmissions().add(submission);
            fileField.clear();
            showAlert("Assignment submitted successfully.");
        });

        VBox box = new VBox(10, table, fileField, submitButton);
        box.setPadding(new Insets(15));
        return box;
    }

    // --- Grades tab: view scores and feedback on graded submissions ---
    private static VBox buildGradesTab(Student student) {
        ObservableList<Submission> mySubmissions = FXCollections.observableArrayList();
        for (Course c : student.getEnrolledCourses()) {
            for (Assignment a : c.getAssignments()) {
                for (Submission s : a.getSubmissions()) {
                    if (s.getStudent() == student) mySubmissions.add(s);
                }
            }
        }

        TableView<Submission> table = new TableView<>(mySubmissions);
        TableColumn<Submission, String> assignCol = new TableColumn<>("Assignment");
        assignCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAssignment().getTitle()));
        TableColumn<Submission, String> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getGrade() == null ? "Not graded yet" : String.valueOf(data.getValue().getGrade().getScore())));
        TableColumn<Submission, String> feedbackCol = new TableColumn<>("Feedback");
        feedbackCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getGrade() == null ? "" : data.getValue().getGrade().getFeedback()));
        table.getColumns().addAll(assignCol, scoreCol, feedbackCol);

        VBox box = new VBox(10, table);
        box.setPadding(new Insets(15));
        return box;
    }

    // --- Announcements tab: read-only feed from enrolled courses ---
    private static VBox buildAnnouncementsTab(Student student) {
        ListView<String> list = new ListView<>();
        for (Course c : student.getEnrolledCourses()) {
            for (Announcement a : c.getAnnouncements()) {
                list.getItems().add("[" + c.getTitle() + "] " + a.getMessage());
            }
        }
        VBox box = new VBox(10, list);
        box.setPadding(new Insets(15));
        return box;
    }

    private static List<TableColumn<Course, ?>> courseColumns() {
        TableColumn<Course, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Course, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        return List.of(titleCol, descCol);
    }

    private static HBox spacer() {
        HBox box = new HBox();
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    private static void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
