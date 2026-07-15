package com.lms.app.data;

import com.lms.app.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

/**
 * Holds all application data in memory (no database yet).
 * This is a singleton so every screen shares the same data.
 * Swap this class out for a real database layer later without touching the UI.
 */
public class DataStore {

    private static final DataStore instance = new DataStore();
    public static DataStore getInstance() { return instance; }

    private final ObservableList<User> users = FXCollections.observableArrayList();
    private final ObservableList<Course> courses = FXCollections.observableArrayList();
    private int nextId = 100;

    private DataStore() {
        seedData();
    }

    /** Pre-loads a few demo accounts and courses so the app is usable immediately. */
    private void seedData() {
        Admin admin = new Admin("A1", "Alex Admin", "admin@lms.com", "admin123");
        Instructor instructor = new Instructor("I1", "Dr. Jamie Cruz", "jamie@lms.com", "teach123");
        Student student1 = new Student("S1", "Sam Rivera", "sam@lms.com", "learn123");
        Student student2 = new Student("S2", "Casey Lee", "casey@lms.com", "learn123");

        users.addAll(admin, instructor, student1, student2);

        Course course1 = new Course("C1", "Object-Oriented Programming 2", "Advanced Java and design patterns", instructor);
        Course course2 = new Course("C2", "Database Systems", "Relational database design and SQL", instructor);
        courses.addAll(course1, course2);
        instructor.getCoursesTaught().addAll(course1, course2);

        student1.enroll(course1);
        student2.enroll(course1);
        student2.enroll(course2);

        Assignment a1 = new Assignment("AS1", "UML Diagram Set", LocalDate.now().plusDays(7), course1);
        Assignment a2 = new Assignment("AS2", "Capstone Proposal", LocalDate.now().minusDays(2), course1);
        course1.getAssignments().addAll(a1, a2);

        course1.getAnnouncements().add(new Announcement("AN1", "Welcome to OOP 2! Please review the syllabus."));
    }

    public ObservableList<User> getUsers() { return users; }
    public ObservableList<Course> getCourses() { return courses; }

    /** Checks email + password against stored users. Returns null if no match (invalid login). */
    public User authenticate(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    /** Simple incrementing ID generator, e.g. generateId("C") -> "C104". */
    public String generateId(String prefix) {
        return prefix + (nextId++);
    }

    public void addUser(User user) { users.add(user); }
    public void removeUser(User user) { users.remove(user); }
    public void addCourse(Course course) { courses.add(course); }
    public void removeCourse(Course course) { courses.remove(course); }
}
