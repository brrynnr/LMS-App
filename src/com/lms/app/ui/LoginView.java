package com.lms.app.ui;

import com.lms.app.Main;
import com.lms.app.data.DataStore;
import com.lms.app.model.*;
import com.lms.app.session.SessionManager;
import com.lms.app.session.SessionService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LoginView {

    // Interface Segregation Principle (ISP)
    // LoginView depends on the SessionService interface,
    // not directly on a specific implementation.
    private static final SessionService sessionService = new SessionManager();

    public static Scene createScene() {

        Label universityLabel = new Label("Brisbane Technological University");
        universityLabel.getStyleClass().add("title-label");

        Label systemLabel = new Label("Learning Management System");
        systemLabel.getStyleClass().add("subtitle-label");

        Label welcomeLabel = new Label("Welcome Back");
        welcomeLabel.getStyleClass().add("welcome-label");

        TextField emailField = new TextField();
        emailField.setPromptText("University Email");
        emailField.setMaxWidth(360);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(360);

        Label statusLabel = new Label();
        statusLabel.getStyleClass().add("error-label");

        Button loginButton = new Button("Sign In");
        loginButton.getStyleClass().add("primary-button");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setPrefHeight(42);

        loginButton.setOnAction(e -> {

            User user = DataStore.getInstance()
                    .authenticate(emailField.getText().trim(),
                            passwordField.getText());

            if (user == null) {
                statusLabel.setText("Invalid email or password.");
                return;
            }

            sessionService.saveSession(user);

            routeToDashboard(user);
        });

        emailField.setOnKeyPressed(e -> {
            if (e.getCode() == javafx.scene.input.KeyCode.ENTER)
                loginButton.fire();
        });

        passwordField.setOnKeyPressed(e -> {
            if (e.getCode() == javafx.scene.input.KeyCode.ENTER)
                loginButton.fire();
        });

        Button registerButton = new Button("Create Account");
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setPrefHeight(42);

        registerButton.setOnAction(e ->
                Main.getPrimaryStage().setScene(RegisterView.createScene()));


        VBox card = new VBox(
                15,
                universityLabel,
                systemLabel,
                new Separator(),
                welcomeLabel,
                emailField,
                passwordField,
                loginButton,
                registerButton,
                statusLabel
        );

        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(45));
        card.setMaxWidth(520);

        card.getStyleClass().add("login-card");

        VBox root = new VBox(card);

        root.setAlignment(Pos.CENTER);

        root.getStyleClass().add("root-pane");

        Scene scene = new Scene(root, 1000, 700);

        Styles.apply(scene);

        return scene;
    }
    private static void routeToDashboard(User user) {

        Main.setSession(user.getUserId(), null);

        Scene scene;

        if (user instanceof Student student) {

            scene = StudentDashboardView.createScene(student);

        } else if (user instanceof Instructor instructor) {

            scene = InstructorDashboardView.createScene(instructor);

        } else {

            scene = AdminDashboardView.createScene((Admin) user);

        }

        Main.getPrimaryStage().setScene(scene);
    }
}