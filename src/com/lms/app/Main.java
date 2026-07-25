package com.lms.app;

import com.lms.app.data.DataStore;
import com.lms.app.model.*;
import com.lms.app.session.SessionManager;
import com.lms.app.session.SessionService;
import com.lms.app.session.UserSession;
import com.lms.app.ui.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage;

    private static final SessionService sessionService = new SessionManager();

    @Override
    public void start(Stage stage) {

        primaryStage = stage;

        stage.setTitle("Brisbane Technological University LMS");
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        Scene startScene = restoreSession();

        if (startScene == null) {
            startScene = LoginView.createScene();
        }

        stage.setScene(startScene);
        stage.show();
    }

    /**
     * Restores the session from session.dat
     */
    private Scene restoreSession() {

        SessionManager manager = new SessionManager();

        UserSession session = sessionService.loadSession();

        if (session == null) {
            return null;
        }

        User user = DataStore.getInstance().findById(session.getUserId());

        if (user == null) {
            return null;
        }

        currentUserId = user.getUserId();

        if (user instanceof Student student) {
            return StudentDashboardView.createScene(student);

        } else if (user instanceof Instructor instructor) {
            return InstructorDashboardView.createScene(instructor);

        } else if (user instanceof Admin admin) {
            return AdminDashboardView.createScene(admin);
        }

        return null;
    }

    // ==========================================================
    // Session Tracking
    // ==========================================================

    private static String currentUserId = null;
    private static String currentTab = null;

    public static void setSession(String userId, String tab) {
        currentUserId = userId;
        currentTab = tab;
    }

    public static void clearSession() {
        currentUserId = null;
        currentTab = null;

        sessionService.clearSession();
    }

    public static String getCurrentUserId() {
        return currentUserId;
    }

    public static String getCurrentTab() {
        return currentTab;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}