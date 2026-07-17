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

public class StudentDashboardView {

    public static Scene createScene(Student student) {
        BorderPane root = new BorderPane();
        root.setTop(buildHeader(student));

        TabPane tabs = buildTabs(student);
        root.setCenter(tabs);
        root.getStyleClass().add("root-pane");

        // Save session on tab change
        tabs.getSelectionModel().selectedItemProperty().addListener((obs, old, now) -> {
            if (now != null) Main.setSession(student.getUserId(), now.getText());
        });

        Scene scene = new Scene(root, 900, 650);
        Styles.apply(scene);
        return scene;
    }

    private static HBox buildHeader(Student student) {
        Label welcome = new Label("Welcome, " + student.getName() + " (Student)");
        welcome.getStyleClass().add("header-label");

        String info = "";
        if (student.getProgram() != null && student.getYearLevel() != null) {
            info = "  |  " + student.getYearLevel() + " - " + student.getProgram();
        }
        Label infoLabel = new Label(info);
        infoLabel.getStyleClass().add("header-sublabel");

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            Main.clearSession();
            Main.getPrimaryStage().setScene(LoginView.createScene());
        });

        HBox header = new HBox(welcome, infoLabel, spacer(), logoutButton);
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

        // Restore last tab from session
        String savedTab = Main.getCurrentTab();
        if (savedTab != null) {
            tabs.getTabs().stream()
                    .filter(t -> t.getText().equals(savedTab))
                    .findFirst()
                    .ifPresent(t -> tabs.getSelectionModel().select(t));
        }
        return tabs;
    }

    private static VBox buildCoursesTab(Student student) {
        // --- Enrolled Courses ---
        Label enrolledLabel = new Label("Enrolled Courses");
        enrolledLabel.getStyleClass().add("section-header");

        TableView<Course> enrolledTable = new TableView<>(student.getEnrolledCourses());

        TableColumn<Course, String> enrolledTitleCol = new TableColumn<>("Course");
        enrolledTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Course, String> enrolledDescCol = new TableColumn<>("Description");
        enrolledDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Course, String> enrolledStatusCol = new TableColumn<>("Status");
        enrolledStatusCol.setCellValueFactory(data -> new SimpleStringProperty("Enrolled"));
        enrolledStatusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                }
            }
        });

        enrolledTable.getColumns().addAll(enrolledTitleCol, enrolledDescCol, enrolledStatusCol);

        // --- Available Courses ---
        Label availableLabel = new Label("Available Courses");
        availableLabel.getStyleClass().add("section-header");

        ObservableList<Course> available = DataStore.getInstance().getAvailableCourses(student);
        TableView<Course> availableTable = new TableView<>(available);

        TableColumn<Course, String> availTitleCol = new TableColumn<>("Course");
        availTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Course, String> availDescCol = new TableColumn<>("Description");
        availDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Course, String> availReqCol = new TableColumn<>("Requirement");
        availReqCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().isPrerequisite() ? "Required" : ""));
        availReqCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.isBlank()) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold;");
                }
            }
        });

        TableColumn<Course, String> availYearCol = new TableColumn<>("Year Level");
        availYearCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getYearLevel()));

        availableTable.getColumns().addAll(availTitleCol, availDescCol, availReqCol, availYearCol);

        Button enrollButton = new Button("Enroll in Selected Course");
        enrollButton.getStyleClass().add("primary-button");
        enrollButton.setOnAction(e -> {
            Course selected = availableTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Select a course from Available Courses first.");
                return;
            }
            if (student.getEnrolledCourses().contains(selected)) {
                showAlert("You are already enrolled in this course.");
                return;
            }
            student.enroll(selected);
            available.remove(selected);
            enrolledTable.refresh();
            Main.setSession(student.getUserId(), "My Courses");
            showAlert("Enrolled in " + selected.getTitle() + ".");
        });

        VBox box = new VBox(8,
                enrolledLabel, enrolledTable,
                new Separator(),
                availableLabel, availableTable, enrollButton);
        box.setPadding(new Insets(15));
        return box;
    }

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
