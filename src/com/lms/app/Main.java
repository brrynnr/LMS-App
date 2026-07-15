package com.lms.app;

import com.lms.app.ui.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        stage.setTitle("LMS - Learning Management System");
        stage.setScene(LoginView.createScene());
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
    }

    /** Lets any screen switch to a different scene on the same window. */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
