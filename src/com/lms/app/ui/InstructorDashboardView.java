package com.lms.app.ui;

import com.lms.app.Main;
import com.lms.app.data.DataStore;
import com.lms.app.model.*;
import javafx.beans.property.SimpleIntegerProperty;
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

public class InstructorDashboardView {

    public static Scene createScene(Instructor instructor) {
        BorderPane root = new BorderPane();
        root.setTop(buildHeader(instructor));

        TabPane tabs = buildTabs(instructor);
        root.setCenter(tabs);
        root.getStyleClass().add("root-pane");

        // Save session on tab change
        tabs.getSelectionModel().selectedItemProperty().addListener((obs, old, now) -> {
            if (now != null) Main.setSession(instructor.getUserId(), now.getText());
        });

        Scene scene = new Scene(root, 900, 650);
        Styles.apply(scene);
        return scene;
    }

    private static HBox buildHeader(Instructor instructor) {
        Label welcome = new Label("Welcome, " + instructor.getName() + " (Instructor)");
        welcome.getStyleClass().add("header-label");

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            Main.clearSession();
            Main.getPrimaryStage().setScene(LoginView.createScene());
        });

        HBox header = new HBox(welcome, spacer(), logoutButton);
        header.setPadding(new Insets(15));
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("header-bar");
        return header;
    }

    private static TabPane buildTabs(Instructor instructor) {
        TabPane tabs = new TabPane();
        tabs.getTabs().addAll(
                new Tab("My Courses", buildCoursesTab(instructor)),
                new Tab("Post Announcement", buildAnnouncementTab(instructor)),
                new Tab("Grade Submissions", buildGradingTab(instructor))
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

    private static VBox buildCoursesTab(Instructor instructor) {
        TableView<Course> table = new TableView<>(instructor.getCoursesTaught());
        TableColumn<Course, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Course, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Course, Number> enrolledCol = new TableColumn<>("Enrolled Students");
        enrolledCol.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getEnrolledStudents().size()));
        table.getColumns().addAll(titleCol, descCol, enrolledCol);

        VBox box = new VBox(10, table);
        box.setPadding(new Insets(15));
        return box;
    }

    // --- Post Announcement tab ---
    private static VBox buildAnnouncementTab(Instructor instructor) {
        ComboBox<Course> courseBox = new ComboBox<>(instructor.getCoursesTaught());
        courseBox.setPromptText("Select a course");

        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Write your announcement here...");
        messageArea.setPrefRowCount(5);

        Button postButton = new Button("Post Announcement");
        postButton.getStyleClass().add("primary-button");
        postButton.setOnAction(e -> {
            Course selected = courseBox.getValue();
            if (selected == null || messageArea.getText().isBlank()) {
                showAlert("Select a course and write a message first.");
                return;
            }
            String id = DataStore.getInstance().generateId("AN");
            selected.getAnnouncements().add(new Announcement(id, messageArea.getText().trim()));
            messageArea.clear();
            showAlert("Announcement posted to " + selected.getTitle() + ".");
        });

        VBox box = new VBox(10, courseBox, messageArea, postButton);
        box.setPadding(new Insets(15));
        return box;
    }

    // --- Grade Submissions tab ---
    private static VBox buildGradingTab(Instructor instructor) {
        ObservableList<Submission> submissions = FXCollections.observableArrayList();
        for (Course c : instructor.getCoursesTaught()) {
            for (Assignment a : c.getAssignments()) {
                submissions.addAll(a.getSubmissions());
            }
        }

        TableView<Submission> table = new TableView<>(submissions);
        TableColumn<Submission, String> studentCol = new TableColumn<>("Student");
        studentCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStudent().getName()));
        TableColumn<Submission, String> assignCol = new TableColumn<>("Assignment");
        assignCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAssignment().getTitle()));
        TableColumn<Submission, String> fileCol = new TableColumn<>("File");
        fileCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFileName()));
        table.getColumns().addAll(studentCol, assignCol, fileCol);

        TextField scoreField = new TextField();
        scoreField.setPromptText("Score (0-100)");
        TextField feedbackField = new TextField();
        feedbackField.setPromptText("Feedback");

        Button gradeButton = new Button("Submit Grade");
        gradeButton.getStyleClass().add("primary-button");
        gradeButton.setOnAction(e -> {
            Submission selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Select a submission first.");
                return;
            }
            double score;
            try {
                score = Double.parseDouble(scoreField.getText().trim());
            } catch (NumberFormatException ex) {
                showAlert("Enter a valid numeric score.");
                return;
            }
            String id = DataStore.getInstance().generateId("G");
            selected.setGrade(new Grade(id, score, feedbackField.getText().trim()));
            scoreField.clear();
            feedbackField.clear();
            showAlert("Grade submitted.");
        });

        VBox box = new VBox(10, table, scoreField, feedbackField, gradeButton);
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
