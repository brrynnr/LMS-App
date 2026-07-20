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

        Label titleLabel = new Label("Learning Management System");
        titleLabel.getStyleClass().add("title-label");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(260);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(260);

        Label statusLabel = new Label();
        statusLabel.getStyleClass().add("error-label");

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("primary-button");
        loginButton.setMaxWidth(260);

        loginButton.setOnAction(e -> {

            User user = DataStore.getInstance()
                    .authenticate(emailField.getText().trim(),
                            passwordField.getText());

            if (user == null) {
                statusLabel.setText("Invalid email or password. Try again.");
                return;
            }

            // ============================
            // Save serialized session
            // ============================
            sessionService.saveSession(user);

            routeToDashboard(user);
        });

        Button registerButton = new Button("Register Account");
        registerButton.setMaxWidth(260);
        registerButton.setOnAction(e ->
                Main.getPrimaryStage().setScene(RegisterView.createScene()));

        Label hint = new Label(
                "Demo accounts:\n" +
                        "Student: sam@lms.com / learn123\n" +
                        "Instructor: jamie@lms.com / teach123\n" +
                        "Admin: admin@lms.com / admin123"
        );
        hint.getStyleClass().add("hint-label");

        VBox card = new VBox(
                12,
                titleLabel,
                emailField,
                passwordField,
                loginButton,
                registerButton,
                statusLabel,
                hint
        );

        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setMaxWidth(360);
        card.getStyleClass().add("login-card");

        VBox root = new VBox(card);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("root-pane");

        Scene scene = new Scene(root, 800, 600);
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