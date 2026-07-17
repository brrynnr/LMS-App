package com.lms.app.ui;

import com.lms.app.Main;
import com.lms.app.data.DataStore;
import com.lms.app.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class AdminDashboardView {

    public static Scene createScene(Admin admin) {
        BorderPane root = new BorderPane();
        root.setTop(buildHeader(admin));

        TabPane tabs = buildTabs(admin);
        root.setCenter(tabs);
        root.getStyleClass().add("root-pane");

        // Save session whenever the admin switches tabs
        tabs.getSelectionModel().selectedItemProperty().addListener((obs, old, now) -> {
            if (now != null) Main.setSession(admin.getUserId(), now.getText());
        });

        Scene scene = new Scene(root, 900, 650);
        Styles.apply(scene);
        return scene;
    }

    private static HBox buildHeader(Admin admin) {
        Label welcome = new Label("Welcome, " + admin.getName() + " (Administrator)");
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

    private static TabPane buildTabs(Admin admin) {
        TabPane tabs = new TabPane();
        tabs.getTabs().addAll(
                new Tab("Manage Users", buildUsersTab()),
                new Tab("Manage Courses", buildCoursesTab())
        );
        tabs.getTabs().forEach(t -> t.setClosable(false));

        // Restore last tab if session had one saved
        String savedTab = Main.getCurrentTab();
        if (savedTab != null) {
            tabs.getTabs().stream()
                    .filter(t -> t.getText().equals(savedTab))
                    .findFirst()
                    .ifPresent(t -> tabs.getSelectionModel().select(t));
        }

        return tabs;
    }

    // --- Manage Users tab ---
    private static VBox buildUsersTab() {
        TableView<User> table = new TableView<>(DataStore.getInstance().getUsers());

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRole()));

        TableColumn<User, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().isActive() ? "Active" : "Deactivated"));

        table.getColumns().addAll(nameCol, emailCol, roleCol, statusCol);

        // Gray out deactivated users in the table
        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null || empty) {
                    setStyle("");
                } else if (!user.isActive()) {
                    setStyle("-fx-text-fill: #aaaaaa; -fx-background-color: #f0f0f0;");
                } else {
                    setStyle("");
                }
            }
        });

        TextField nameField     = new TextField();  nameField.setPromptText("Full name");
        TextField emailField    = new TextField();  emailField.setPromptText("Email");
        TextField passwordField = new TextField();  passwordField.setPromptText("Password");
        ComboBox<String> roleBox = new ComboBox<>(
                FXCollections.observableArrayList("Student", "Instructor", "Admin"));
        roleBox.setPromptText("Role");

        Button addButton = new Button("Add User");
        addButton.getStyleClass().add("primary-button");
        addButton.setOnAction(e -> {
            if (nameField.getText().isBlank() || emailField.getText().isBlank()
                    || passwordField.getText().isBlank() || roleBox.getValue() == null) {
                showAlert("Fill in all fields before adding a user.");
                return;
            }
            String id = DataStore.getInstance().generateId("U");
            User newUser = switch (roleBox.getValue()) {
                case "Instructor" -> new Instructor(id, nameField.getText(), emailField.getText(), passwordField.getText());
                case "Admin"      -> new Admin(id, nameField.getText(), emailField.getText(), passwordField.getText());
                default           -> new Student(id, nameField.getText(), emailField.getText(), passwordField.getText());
            };
            DataStore.getInstance().addUser(newUser);
            nameField.clear();
            emailField.clear();
            passwordField.clear();
            roleBox.setValue(null);
        });

        // Soft-delete: marks user inactive, keeps the DB row with is_active = 0
        Button removeButton = new Button("Deactivate Selected");
        removeButton.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Select a user to deactivate.");
                return;
            }
            if (!selected.isActive()) {
                showAlert(selected.getName() + " is already deactivated.");
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Deactivate " + selected.getName() + "? They will no longer be able to log in.",
                    ButtonType.YES, ButtonType.NO);
            confirm.setHeaderText("Confirm Deactivation");
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.YES) {
                    DataStore.getInstance().removeUser(selected);
                    table.refresh();
                    showAlert(selected.getName() + " has been deactivated.");
                }
            });
        });

        HBox form = new HBox(10, nameField, emailField, passwordField, roleBox, addButton, removeButton);
        form.setAlignment(Pos.CENTER_LEFT);

        VBox box = new VBox(10, table, form);
        box.setPadding(new Insets(15));
        return box;
    }

    // --- Manage Courses tab ---
    private static VBox buildCoursesTab() {
        TableView<Course> table = new TableView<>(DataStore.getInstance().getCourses());

        TableColumn<Course, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Course, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Course, String> instructorCol = new TableColumn<>("Instructor");
        instructorCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getInstructor() == null ? "Unassigned" : data.getValue().getInstructor().getName()));
        table.getColumns().addAll(titleCol, descCol, instructorCol);

        TextField titleField = new TextField(); titleField.setPromptText("Course title");
        TextField descField  = new TextField(); descField.setPromptText("Description");

        Button addButton = new Button("Add Course");
        addButton.getStyleClass().add("primary-button");
        addButton.setOnAction(e -> {
            if (titleField.getText().isBlank()) {
                showAlert("Enter a course title.");
                return;
            }
            String id = DataStore.getInstance().generateId("C");
            DataStore.getInstance().addCourse(
                    new Course(id, titleField.getText().trim(), descField.getText().trim(), null));
            titleField.clear();
            descField.clear();
        });

        Button removeButton = new Button("Remove Selected");
        removeButton.setOnAction(e -> {
            Course selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Select a course to remove.");
                return;
            }
            DataStore.getInstance().removeCourse(selected);
        });

        HBox form = new HBox(10, titleField, descField, addButton, removeButton);
        form.setAlignment(Pos.CENTER_LEFT);

        VBox box = new VBox(10, table, form);
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
