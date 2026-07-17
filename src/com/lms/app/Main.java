package com.lms.app;

import com.lms.app.data.DataStore;
import com.lms.app.data.DatabaseManager;
import com.lms.app.model.*;
import com.lms.app.ui.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        stage.setTitle("LMS - Learning Management System");
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        // Try to restore the last session from the database
        Scene startScene = restoreSession();
        if (startScene == null) {
            startScene = LoginView.createScene();
        }

        stage.setScene(startScene);
        stage.show();

        // Save session whenever the window is closed.
        // If no user is logged in (login/register page), clear the session
        // so the app returns to Login on next open instead of a broken state.
        stage.setOnCloseRequest(e -> {
            if (getCurrentUserId() != null) {
                DatabaseManager.getInstance().saveSession(getCurrentUserId(), getCurrentTab());
            } else {
                DatabaseManager.getInstance().clearSession();
            }
        });
    }

    // ---------------------------------------------------------------
    // Session restore
    // ---------------------------------------------------------------

    /**
     * Reads the last saved session from MySQL.
     * Returns the correct dashboard Scene if a valid active user is found,
     * or null to fall back to the login screen.
     */
    private Scene restoreSession() {
        String[] session = DatabaseManager.getInstance().loadSession();
        String userId  = session[0];
        String lastTab = session[1];

        if (userId == null) return null;

        User user = DataStore.getInstance().findById(userId);
        if (user == null || !user.isActive()) return null;

        // Remember which tab to re-open
        currentTab    = lastTab;
        currentUserId = userId;

        if (user instanceof Student student) {
            return StudentDashboardView.createScene(student);
        } else if (user instanceof Instructor instructor) {
            return InstructorDashboardView.createScene(instructor);
        } else if (user instanceof Admin admin) {
            return AdminDashboardView.createScene(admin);
        }
        return null;
    }

    // ---------------------------------------------------------------
    // Session tracking — updated by dashboard views on tab change / logout
    // ---------------------------------------------------------------

    private static String currentUserId = null;
    private static String currentTab    = null;

    public static void setSession(String userId, String tab) {
        currentUserId = userId;
        currentTab    = tab;
        DatabaseManager.getInstance().saveSession(userId, tab);
    }

    public static void clearSession() {
        currentUserId = null;
        currentTab    = null;
        DatabaseManager.getInstance().clearSession();
    }

    public static String getCurrentUserId() { return currentUserId; }
    public static String getCurrentTab()    { return currentTab; }

    // ---------------------------------------------------------------

    public static Stage getPrimaryStage() { return primaryStage; }

    public static void main(String[] args) {
        launch(args);
    }
}
