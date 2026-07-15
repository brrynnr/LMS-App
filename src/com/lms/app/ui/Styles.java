package com.lms.app.ui;

import javafx.scene.Scene;

/**
 * Applies styles.css to a scene without crashing the app if the file
 * can't be found on the classpath (e.g. a resources-folder misconfiguration).
 * If you see the warning below in your console, check that
 * src/main/resources/styles.css exists with exactly that name.
 */
public class Styles {

    public static void apply(Scene scene) {
        var url = Styles.class.getResource("/styles.css");
        if (url != null) {
            scene.getStylesheets().add(url.toExternalForm());
        } else {
            System.err.println(
                "WARNING: styles.css not found on classpath. " +
                "The app will still run, just without styling. " +
                "Check that src/main/resources/styles.css exists (exact name, no hidden .txt extension) " +
                "and that target/classes/styles.css exists after building."
            );
        }
    }
}
