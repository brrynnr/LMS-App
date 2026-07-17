package com.lms.app.data;

import com.lms.app.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all MySQL database operations for the LMS application.
 *
 * Edit DB_URL, DB_USER, DB_PASS below to match your MySQL setup.
 * All tables are created automatically on first run.
 */
public class DatabaseManager {

    // ---------------------------------------------------------------
    // EDIT THESE to match your MySQL server
    // ---------------------------------------------------------------
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/lms_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    // ---------------------------------------------------------------

    private static final DatabaseManager instance = new DatabaseManager();
    public static DatabaseManager getInstance() { return instance; }

    private Connection connection;

    private DatabaseManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            createTablesIfNotExist();
        } catch (Exception e) {
            System.err.println("[DB] Could not connect to MySQL: " + e.getMessage());
            System.err.println("[DB] App will run with in-memory data only.");
            connection = null;
        }
    }

    public boolean isConnected() {
        return connection != null;
    }

    // ---------------------------------------------------------------
    // Schema creation — runs once on first launch
    // ---------------------------------------------------------------
    private void createTablesIfNotExist() throws SQLException {
        try (Statement st = connection.createStatement()) {

            st.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    user_id   VARCHAR(20)  PRIMARY KEY,
                    name      VARCHAR(100) NOT NULL,
                    email     VARCHAR(100) NOT NULL UNIQUE,
                    password  VARCHAR(100) NOT NULL,
                    role      VARCHAR(20)  NOT NULL,
                    is_active TINYINT(1)   NOT NULL DEFAULT 1
                )
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS courses (
                    course_id     VARCHAR(20)  PRIMARY KEY,
                    title         VARCHAR(200) NOT NULL,
                    description   TEXT,
                    instructor_id VARCHAR(20),
                    FOREIGN KEY (instructor_id) REFERENCES users(user_id)
                )
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS enrollments (
                    student_id VARCHAR(20) NOT NULL,
                    course_id  VARCHAR(20) NOT NULL,
                    PRIMARY KEY (student_id, course_id),
                    FOREIGN KEY (student_id) REFERENCES users(user_id),
                    FOREIGN KEY (course_id)  REFERENCES courses(course_id)
                )
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS assignments (
                    assignment_id VARCHAR(20)  PRIMARY KEY,
                    title         VARCHAR(200) NOT NULL,
                    due_date      DATE,
                    course_id     VARCHAR(20),
                    FOREIGN KEY (course_id) REFERENCES courses(course_id)
                )
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS announcements (
                    announcement_id VARCHAR(20)  PRIMARY KEY,
                    message         TEXT         NOT NULL,
                    date_posted     DATE         NOT NULL,
                    course_id       VARCHAR(20),
                    FOREIGN KEY (course_id) REFERENCES courses(course_id)
                )
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS submissions (
                    submission_id  VARCHAR(20)  PRIMARY KEY,
                    student_id     VARCHAR(20)  NOT NULL,
                    assignment_id  VARCHAR(20)  NOT NULL,
                    file_name      VARCHAR(200),
                    submitted_date DATE         NOT NULL,
                    FOREIGN KEY (student_id)    REFERENCES users(user_id),
                    FOREIGN KEY (assignment_id) REFERENCES assignments(assignment_id)
                )
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS grades (
                    grade_id      VARCHAR(20) PRIMARY KEY,
                    submission_id VARCHAR(20) NOT NULL UNIQUE,
                    score         DOUBLE      NOT NULL,
                    feedback      TEXT,
                    FOREIGN KEY (submission_id) REFERENCES submissions(submission_id)
                )
            """);

            // Stores the last logged-in user and tab so the app restores on next open
            st.execute("""
                CREATE TABLE IF NOT EXISTS app_session (
                    id       INT         PRIMARY KEY DEFAULT 1,
                    user_id  VARCHAR(20),
                    last_tab VARCHAR(50)
                )
            """);

            st.execute("""
                INSERT IGNORE INTO app_session (id, user_id, last_tab) VALUES (1, NULL, NULL)
            """);

            // Add columns for student/instructor details if they don't exist (safe migration)
            addColumnIfNotExist(st, "users", "year_level",  "VARCHAR(50)");
            addColumnIfNotExist(st, "users", "program",     "VARCHAR(100)");
            addColumnIfNotExist(st, "users", "department",  "VARCHAR(100)");
            addColumnIfNotExist(st, "users", "employee_id", "VARCHAR(30)");
            addColumnIfNotExist(st, "users", "designation", "VARCHAR(50)");

            addColumnIfNotExist(st, "courses", "program",        "VARCHAR(100)");
            addColumnIfNotExist(st, "courses", "year_level",     "VARCHAR(50)");
            addColumnIfNotExist(st, "courses", "is_prerequisite", "TINYINT(1) DEFAULT 0");
        }
    }

    private void addColumnIfNotExist(Statement st, String table, String column, String type) throws SQLException {
        try (ResultSet rs = st.executeQuery(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = '" + table + "' AND COLUMN_NAME = '" + column + "'")) {
            if (rs.next() && rs.getInt(1) == 0) {
                st.executeUpdate("ALTER TABLE " + table + " ADD COLUMN " + column + " " + type);
            }
        }
    }

    // ---------------------------------------------------------------
    // ID counter
    // ---------------------------------------------------------------
    public int getNextId() {
        if (connection == null) return 100;
        String sql = """
            SELECT COALESCE(MAX(n), 99) + 1 FROM (
                SELECT CAST(SUBSTRING(user_id, 2) AS UNSIGNED) n
                  FROM users WHERE user_id REGEXP '^[A-Z][0-9]+$'
                UNION ALL
                SELECT CAST(SUBSTRING(course_id, 2) AS UNSIGNED)
                  FROM courses WHERE course_id REGEXP '^[A-Z][0-9]+$'
                UNION ALL
                SELECT CAST(SUBSTRING(assignment_id, 2) AS UNSIGNED)
                  FROM assignments WHERE assignment_id REGEXP '^[A-Z]{2}[0-9]+$'
                UNION ALL
                SELECT CAST(SUBSTRING(announcement_id, 2) AS UNSIGNED)
                  FROM announcements WHERE announcement_id REGEXP '^[A-Z]{2}[0-9]+$'
            ) ids
        """;
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[DB] getNextId failed: " + e.getMessage());
        }
        return 100;
    }

    // ---------------------------------------------------------------
    // Load all data on startup
    // ---------------------------------------------------------------
    public List<User> loadUsers() {
        List<User> list = new ArrayList<>();
        if (connection == null) return list;
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT user_id, name, email, password, role, is_active, " +
                     "year_level, program, department, employee_id, designation FROM users")) {
            while (rs.next()) {
                String id     = rs.getString("user_id");
                String name   = rs.getString("name");
                String email  = rs.getString("email");
                String pass   = rs.getString("password");
                String role   = rs.getString("role");
                boolean active = rs.getBoolean("is_active");
                User u = switch (role) {
                    case "Instructor"    -> {
                        Instructor i = new Instructor(id, name, email, pass);
                        i.setDepartment(rs.getString("department"));
                        i.setEmployeeId(rs.getString("employee_id"));
                        i.setDesignation(rs.getString("designation"));
                        yield i;
                    }
                    case "Administrator" -> new Admin(id, name, email, pass);
                    default              -> {
                        Student s = new Student(id, name, email, pass);
                        s.setYearLevel(rs.getString("year_level"));
                        s.setProgram(rs.getString("program"));
                        yield s;
                    }
                };
                u.setActive(active);
                list.add(u);
            }
        } catch (SQLException e) {
            System.err.println("[DB] loadUsers failed: " + e.getMessage());
        }
        return list;
    }

    public List<Course> loadCourses(List<User> users) {
        List<Course> list = new ArrayList<>();
        if (connection == null) return list;
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT course_id, title, description, instructor_id, " +
                     "program, year_level, is_prerequisite FROM courses")) {
            while (rs.next()) {
                String cid   = rs.getString("course_id");
                String title = rs.getString("title");
                String desc  = rs.getString("description");
                String iid   = rs.getString("instructor_id");
                Instructor instructor = users.stream()
                        .filter(u -> u.getUserId().equals(iid) && u instanceof Instructor)
                        .map(u -> (Instructor) u)
                        .findFirst().orElse(null);
                Course c = new Course(cid, title, desc == null ? "" : desc, instructor);
                c.setProgram(rs.getString("program"));
                c.setYearLevel(rs.getString("year_level"));
                c.setPrerequisite(rs.getBoolean("is_prerequisite"));
                list.add(c);
            }
        } catch (SQLException e) {
            System.err.println("[DB] loadCourses failed: " + e.getMessage());
        }
        return list;
    }

    public void loadEnrollments(List<User> users, List<Course> courses) {
        if (connection == null) return;
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("SELECT student_id, course_id FROM enrollments")) {
            while (rs.next()) {
                String sid = rs.getString("student_id");
                String cid = rs.getString("course_id");
                users.stream()
                        .filter(u -> u.getUserId().equals(sid) && u instanceof Student)
                        .map(u -> (Student) u)
                        .findFirst()
                        .ifPresent(student -> courses.stream()
                                .filter(c -> c.getCourseId().equals(cid))
                                .findFirst()
                                .ifPresent(student::enroll));
            }
        } catch (SQLException e) {
            System.err.println("[DB] loadEnrollments failed: " + e.getMessage());
        }
    }

    public void loadAssignments(List<Course> courses) {
        if (connection == null) return;
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT assignment_id, title, due_date, course_id FROM assignments")) {
            while (rs.next()) {
                String aid   = rs.getString("assignment_id");
                String title = rs.getString("title");
                Date d       = rs.getDate("due_date");
                LocalDate due = (d != null) ? d.toLocalDate() : LocalDate.now().plusDays(7);
                String cid   = rs.getString("course_id");
                courses.stream()
                        .filter(c -> c.getCourseId().equals(cid))
                        .findFirst()
                        .ifPresent(course -> course.getAssignments()
                                .add(new Assignment(aid, title, due, course)));
            }
        } catch (SQLException e) {
            System.err.println("[DB] loadAssignments failed: " + e.getMessage());
        }
    }

    public void loadAnnouncements(List<Course> courses) {
        if (connection == null) return;
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT announcement_id, message, course_id FROM announcements")) {
            while (rs.next()) {
                String anid = rs.getString("announcement_id");
                String msg  = rs.getString("message");
                String cid  = rs.getString("course_id");
                courses.stream()
                        .filter(c -> c.getCourseId().equals(cid))
                        .findFirst()
                        .ifPresent(course -> course.getAnnouncements()
                                .add(new Announcement(anid, msg)));
            }
        } catch (SQLException e) {
            System.err.println("[DB] loadAnnouncements failed: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // Save / persist
    // ---------------------------------------------------------------
    public void saveUser(User user) {
        if (connection == null) return;
        String sql = """
            INSERT INTO users (user_id, name, email, password, role, is_active,
                               year_level, program, department, employee_id, designation)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                name=VALUES(name), email=VALUES(email),
                password=VALUES(password), is_active=VALUES(is_active),
                year_level=VALUES(year_level), program=VALUES(program),
                department=VALUES(department), employee_id=VALUES(employee_id),
                designation=VALUES(designation)
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getUserId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getRole());
            ps.setBoolean(6, user.isActive());

            if (user instanceof Student s) {
                ps.setString(7, s.getYearLevel());
                ps.setString(8, s.getProgram());
                ps.setString(9, null);
                ps.setString(10, null);
                ps.setString(11, null);
            } else if (user instanceof Instructor i) {
                ps.setString(7, null);
                ps.setString(8, null);
                ps.setString(9, i.getDepartment());
                ps.setString(10, i.getEmployeeId());
                ps.setString(11, i.getDesignation());
            } else {
                ps.setString(7, null);
                ps.setString(8, null);
                ps.setString(9, null);
                ps.setString(10, null);
                ps.setString(11, null);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] saveUser failed: " + e.getMessage());
        }
    }

    /** Soft-delete: sets is_active = 0 — row is kept, user cannot log in. */
    public void softDeleteUser(User user) {
        if (connection == null) return;
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE users SET is_active = 0 WHERE user_id = ?")) {
            ps.setString(1, user.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] softDeleteUser failed: " + e.getMessage());
        }
    }

    public void saveCourse(Course course) {
        if (connection == null) return;
        String sql = """
            INSERT INTO courses (course_id, title, description, instructor_id,
                                 program, year_level, is_prerequisite)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                title=VALUES(title), description=VALUES(description),
                instructor_id=VALUES(instructor_id),
                program=VALUES(program), year_level=VALUES(year_level),
                is_prerequisite=VALUES(is_prerequisite)
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, course.getCourseId());
            ps.setString(2, course.getTitle());
            ps.setString(3, course.getDescription());
            ps.setString(4, course.getInstructor() == null ? null : course.getInstructor().getUserId());
            ps.setString(5, course.getProgram());
            ps.setString(6, course.getYearLevel());
            ps.setBoolean(7, course.isPrerequisite());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] saveCourse failed: " + e.getMessage());
        }
    }

    public void deleteCourse(Course course) {
        if (connection == null) return;
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM courses WHERE course_id = ?")) {
            ps.setString(1, course.getCourseId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] deleteCourse failed: " + e.getMessage());
        }
    }

    public void saveAnnouncement(Announcement ann, String courseId) {
        if (connection == null) return;
        String sql = """
            INSERT IGNORE INTO announcements (announcement_id, message, date_posted, course_id)
            VALUES (?, ?, ?, ?)
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, ann.getAnnouncementId());
            ps.setString(2, ann.getMessage());
            ps.setDate(3, Date.valueOf(ann.getDatePosted()));
            ps.setString(4, courseId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] saveAnnouncement failed: " + e.getMessage());
        }
    }

    public void saveGrade(Grade grade, String submissionId) {
        if (connection == null) return;
        String sql = """
            INSERT INTO grades (grade_id, submission_id, score, feedback)
            VALUES (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE score=VALUES(score), feedback=VALUES(feedback)
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, grade.getGradeId());
            ps.setString(2, submissionId);
            ps.setDouble(3, grade.getScore());
            ps.setString(4, grade.getFeedback());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] saveGrade failed: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // Session persistence
    // ---------------------------------------------------------------

    /** Saves the current user + tab so the app can restore it on next open. */
    public void saveSession(String userId, String lastTab) {
        if (connection == null) return;
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE app_session SET user_id = ?, last_tab = ? WHERE id = 1")) {
            ps.setString(1, userId);
            ps.setString(2, lastTab);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] saveSession failed: " + e.getMessage());
        }
    }

    /** Clears the session on logout. */
    public void clearSession() {
        if (connection == null) return;
        try (Statement st = connection.createStatement()) {
            st.executeUpdate(
                    "UPDATE app_session SET user_id = NULL, last_tab = NULL WHERE id = 1");
        } catch (SQLException e) {
            System.err.println("[DB] clearSession failed: " + e.getMessage());
        }
    }

    /**
     * Returns [userId, lastTab]. Both null if no session was saved.
     */
    public String[] loadSession() {
        String[] result = {null, null};
        if (connection == null) return result;
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT user_id, last_tab FROM app_session WHERE id = 1")) {
            if (rs.next()) {
                result[0] = rs.getString("user_id");
                result[1] = rs.getString("last_tab");
            }
        } catch (SQLException e) {
            System.err.println("[DB] loadSession failed: " + e.getMessage());
        }
        return result;
    }
}
