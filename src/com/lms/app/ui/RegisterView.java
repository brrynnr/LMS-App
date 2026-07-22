package com.lms.app.ui;

import com.lms.app.Main;
import com.lms.app.data.DataStore;
import com.lms.app.data.DatabaseManager;
import com.lms.app.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class RegisterView {

    private static final String[] YEAR_LEVELS = {
        "1st Year", "2nd Year", "3rd Year", "4th Year", "5th Year"
    };

    private static final String[] PROGRAMS = {
        "BS Computer Science",
        "BS Information Technology",
        "BS Electrical Engineering",
        "BS Mechanical Engineering",
        "BS Civil Engineering",
        "BS Chemical Engineering",
        "BS Architecture",
        "BS Accountancy",
        "BS Business Administration",
        "BS Marketing Management",
        "BS Office Administration",
        "BA Communication",
        "BA Political Science",
        "BA Psychology",
        "BS Psychology",
        "BS Education (Major in English)",
        "BS Education (Major in Mathematics)",
        "BS Education (Major in Science)",
        "BS Elementary Education",
        "BS Secondary Education",
        "BS Nursing",
        "BS Pharmacy",
        "BS Medical Technology",
        "BS Physical Therapy",
        "BS Tourism Management",
        "BS Hospitality Management",
        "BS Restaurant Management",
        "BS Criminology",
        "BS Social Work",
        "BS Maritime Studies",
        "BS Agriculture",
        "BS Environmental Science",
        "BS Forestry",
        "BS Fisheries",
        "BS Veterinary Medicine",
        "Doctor of Medicine",
        "BS Dentistry",
        "BS Midwifery",
        "BA Philosophy",
        "BS Mathematics",
        "BS Physics",
        "BS Chemistry",
        "BS Biology",
        "BS Marine Biology"
    };

    private static final String[] DEPARTMENTS = {
        "College of Computer Studies",
        "College of Engineering",
        "College of Architecture",
        "College of Accountancy and Business",
        "College of Education",
        "College of Nursing",
        "College of Pharmacy",
        "College of Arts and Sciences",
        "College of Tourism and Hospitality",
        "College of Criminology",
        "College of Agriculture",
        "College of Maritime Studies",
        "College of Medicine",
        "College of Dentistry",
        "Graduate School"
    };

    private static final String[] DESIGNATIONS = {
        "Professor I",
        "Professor II",
        "Professor III",
        "Professor IV",
        "Professor V",
        "Associate Professor I",
        "Associate Professor II",
        "Associate Professor III",
        "Assistant Professor I",
        "Assistant Professor II",
        "Assistant Professor III",
        "Instructor I",
        "Instructor II",
        "Instructor III",
        "Lecturer",
        "Visiting Professor"
    };

    public static Scene createScene() {
        Label titleLabel = new Label("Create an Account");
        titleLabel.getStyleClass().add("title-label");

        // --- Common fields ---
        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        nameField.setMaxWidth(260);

        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");
        emailField.setMaxWidth(260);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(260);

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.setMaxWidth(260);

        // --- Account type selector ---
        ComboBox<String> roleBox = new ComboBox<>(
                FXCollections.observableArrayList("Student", "Instructor", "Admin"));
        roleBox.setPromptText("Account Type");
        roleBox.setMaxWidth(260);

        Label dbWarningLabel = new Label();
        dbWarningLabel.getStyleClass().add("error-label");
        dbWarningLabel.setWrapText(true);
        dbWarningLabel.setMaxWidth(260);
        if (!DatabaseManager.getInstance().isConnected()) {
            dbWarningLabel.setText("Warning: Database not connected. Accounts will not persist after restart.");
        }

        // --- Student fields (shown when Student is selected) ---
        Label studentHeaderLabel = new Label("Student Information");
        studentHeaderLabel.getStyleClass().add("section-header");

        ComboBox<String> yearLevelBox = createAutocompleteCombo(YEAR_LEVELS);
        yearLevelBox.setPromptText("Year Level");
        yearLevelBox.setMaxWidth(260);

        ComboBox<String> programBox = createAutocompleteCombo(PROGRAMS);
        programBox.setPromptText("Program / Course");
        programBox.setMaxWidth(260);

        VBox studentFields = new VBox(8, studentHeaderLabel, yearLevelBox, programBox);
        studentFields.setVisible(false);
        studentFields.setManaged(false);

        // --- Instructor fields (shown when Instructor is selected) ---
        Label instructorHeaderLabel = new Label("Instructor Information");
        instructorHeaderLabel.getStyleClass().add("section-header");

        ComboBox<String> departmentBox = createAutocompleteCombo(DEPARTMENTS);
        departmentBox.setPromptText("Department / College");
        departmentBox.setMaxWidth(260);

        ComboBox<String> designationBox = createAutocompleteCombo(DESIGNATIONS);
        designationBox.setPromptText("Designation / Rank");
        designationBox.setMaxWidth(260);

        VBox instructorFields = new VBox(8, instructorHeaderLabel, departmentBox, designationBox);
        instructorFields.setVisible(false);
        instructorFields.setManaged(false);

        // --- Separator between sections ---
        Separator separator = new Separator();
        separator.setVisible(false);
        separator.setManaged(false);

        // --- Toggle student/instructor fields based on role ---
        roleBox.setOnAction(e -> {
            String role = roleBox.getValue();
            boolean isStudent = "Student".equals(role);
            boolean isInstructor = "Instructor".equals(role);
            boolean showExtra = isStudent || isInstructor;

            studentFields.setVisible(isStudent);
            studentFields.setManaged(isStudent);

            instructorFields.setVisible(isInstructor);
            instructorFields.setManaged(isInstructor);

            separator.setVisible(showExtra);
            separator.setManaged(showExtra);
        });

        Label statusLabel = new Label();
        statusLabel.getStyleClass().add("error-label");
        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(260);

        // --- Register button ---
        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("primary-button");
        registerButton.setMaxWidth(260);
        registerButton.setOnAction(e -> {
            String name     = nameField.getText().trim();
            String email    = emailField.getText().trim();
            String password = passwordField.getText();
            String confirm  = confirmPasswordField.getText();
            String role     = roleBox.getValue();

            // Validation
            if (name.isBlank() || email.isBlank() || password.isBlank()
                    || confirm.isBlank() || role == null) {
                statusLabel.setText("Please fill in all fields and select an account type.");
                return;
            }
            if (!email.contains("@") || !email.contains(".")) {
                statusLabel.setText("Please enter a valid email address.");
                return;
            }
            if (password.length() < 6) {
                statusLabel.setText("Password must be at least 6 characters.");
                return;
            }
            if (!password.equals(confirm)) {
                statusLabel.setText("Passwords do not match.");
                return;
            }

            // Validate student-specific fields
            if ("Student".equals(role)) {
                if (yearLevelBox.getValue() == null || yearLevelBox.getValue().isBlank()) {
                    statusLabel.setText("Please select your year level.");
                    return;
                }
                if (programBox.getValue() == null || programBox.getValue().isBlank()) {
                    statusLabel.setText("Please select your program.");
                    return;
                }
            }

            // Validate instructor-specific fields
            if ("Instructor".equals(role)) {
                if (departmentBox.getValue() == null || departmentBox.getValue().isBlank()) {
                    statusLabel.setText("Please select your department.");
                    return;
                }
                if (designationBox.getValue() == null || designationBox.getValue().isBlank()) {
                    statusLabel.setText("Please select your designation.");
                    return;
                }
            }

            // Check if email is already taken
            for (User u : DataStore.getInstance().getUsers()) {
                if (u.getEmail().equalsIgnoreCase(email)) {
                    statusLabel.setText("An account with that email already exists.");
                    return;
                }
            }

            // Create the correct user type and save to MySQL
            String prefix = switch (role) {
                case "Instructor" -> "I";
                case "Admin"      -> "A";
                default           -> "S";
            };
            String id = DataStore.getInstance().generateId(prefix);

            User newUser = switch (role) {
                case "Instructor" -> {
                    Instructor i = new Instructor(id, name, email, password);
                    i.setDepartment(departmentBox.getValue().trim());
                    i.setDesignation(designationBox.getValue().trim());
                    yield i;
                }
                case "Admin" -> new Admin(id, name, email, password);
                default -> {
                    Student s = new Student(id, name, email, password);
                    s.setYearLevel(yearLevelBox.getValue().trim());
                    s.setProgram(programBox.getValue().trim());
                    yield s;
                }
            };
            DataStore.getInstance().addUser(newUser);

            boolean dbSaved = DatabaseManager.getInstance().isConnected();
            String successMsg = dbSaved
                ? "Account created successfully!\nYou can now log in with your credentials."
                : "Account created successfully!\nYou can now log in with your credentials.\nNote: Database not connected. Account will not persist after restart.";
            Alert alert = new Alert(Alert.AlertType.INFORMATION, successMsg, ButtonType.OK);
            alert.setHeaderText("Registration Successful");
            alert.showAndWait();

            Main.getPrimaryStage().setScene(LoginView.createScene());
        });

        // --- Back to Login link ---
        Hyperlink backLink = new Hyperlink("Already have an account? Log in");
        backLink.setOnAction(e -> Main.getPrimaryStage().setScene(LoginView.createScene()));

        // --- Layout ---
        VBox card = new VBox(10,
                titleLabel,
                nameField,
                emailField,
                passwordField,
                confirmPasswordField,
                roleBox,
                dbWarningLabel,
                separator,
                studentFields,
                instructorFields,
                registerButton,
                statusLabel,
                backLink);
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

    /**
     * Creates an editable ComboBox with filtering behavior.
     * As the user types, the dropdown filters to show matching items.
     * The user can freely change their selection at any time.
     */
    private static ComboBox<String> createAutocompleteCombo(String[] items) {
        ObservableList<String> options = FXCollections.observableArrayList(items);
        FilteredList<String> filteredItems = new FilteredList<>(options, p -> true);

        ComboBox<String> combo = new ComboBox<>(filteredItems);
        combo.setEditable(true);

        TextField editor = combo.getEditor();

        editor.textProperty().addListener((obs, oldText, newText) -> {
            if (newText == null || newText.isBlank()) {
                filteredItems.setPredicate(p -> true);
                combo.getSelectionModel().clearSelection();
                if (editor.isFocused()) combo.show();
                return;
            }

            String lower = newText.toLowerCase();
            filteredItems.setPredicate(item -> item.toLowerCase().contains(lower));

            if (filteredItems.isEmpty()) {
                combo.hide();
            } else if (editor.isFocused()) {
                combo.show();
            }
        });

        combo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals(editor.getText())) {
                editor.setText(newVal);
            }
        });

        return combo;
    }
}
