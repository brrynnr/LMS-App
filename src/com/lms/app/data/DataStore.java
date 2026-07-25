package com.lms.app.data;

import com.lms.app.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Central data store for the LMS application.
 * On startup it tries to load everything from MySQL via DatabaseManager.
 * If the DB is unavailable it falls back to hard-coded seed data so the
 * app still runs.
 */
public class DataStore {

    private static final DataStore instance = new DataStore();
    public static DataStore getInstance() { return instance; }

    private final ObservableList<User> users   = FXCollections.observableArrayList();
    private final ObservableList<Course> courses = FXCollections.observableArrayList();
    private int nextId = 100;

    private DataStore() {
        DatabaseManager db = DatabaseManager.getInstance();
        if (db.isConnected()) {
            loadFromDatabase(db);
        } else {
            seedData();
        }
    }

    // ---------------------------------------------------------------
    // Load from MySQL
    // ---------------------------------------------------------------
    private void loadFromDatabase(DatabaseManager db) {
        List<User>   dbUsers   = db.loadUsers();
        List<Course> dbCourses = db.loadCourses(dbUsers);
        db.loadEnrollments(dbUsers, dbCourses);
        db.loadAssignments(dbCourses);
        db.loadAnnouncements(dbCourses);

        for (Course c : dbCourses) {
            if (c.getInstructor() != null) {
                ObservableList<Course> taught = c.getInstructor().getCoursesTaught();

                if (!taught.contains(c)) {
                    taught.add(c);
                }

                System.out.println(
                        c.getTitle() + " added to " +
                                c.getInstructor().getName() +
                                " | total = " +
                                c.getInstructor().getCoursesTaught().size()
                );
            } else {
                System.out.println(
                        "Course " + c.getTitle() + " has NO instructor."
                );
            }
        }

        users.addAll(dbUsers);
        courses.addAll(dbCourses);

        // Set next ID from DB so generated IDs don't collide
        nextId = db.getNextId();

        // If DB is empty or has no courses, seed with demo data
        if (users.isEmpty() || courses.isEmpty()) {
            seedData();
            persistSeedData(db);
        }
    }

    /** Writes all seeded demo data into MySQL so it persists. */
    private void persistSeedData(DatabaseManager db) {
        for (User u : users)     db.saveUser(u);
        for (Course c : courses) db.saveCourse(c);
    }

    // ---------------------------------------------------------------
    // In-memory fallback seed data — full curriculum catalog
    // ---------------------------------------------------------------
    private void seedData() {
        // Use existing objects if already loaded, otherwise create new ones
        Instructor instructor = (Instructor) findUser("I1");
        if (instructor == null) {
            instructor = new Instructor("I1", "Dr. Jamie Cruz", "jamie@lms.com", "teach123");
            instructor.setDepartment("College of Computer Studies");
            instructor.setDesignation("Professor III");
            users.add(instructor);
        }

        Instructor instructor2 = (Instructor) findUser("I2");
        if (instructor2 == null) {
            instructor2 = new Instructor("I2", "Prof. Maria Santos", "maria@lms.com", "teach123");
            instructor2.setDepartment("College of Engineering");
            instructor2.setDesignation("Professor II");
            users.add(instructor2);
        }

        if (!hasUser("A1")) {
            users.add(new Admin("A1", "Alex Admin", "admin@lms.com", "admin123"));
        }
        if (!hasUser("S1")) {
            Student s1 = new Student("S1", "Sam Rivera", "sam@lms.com", "learn123");
            s1.setYearLevel("3rd Year");
            s1.setProgram("BS Computer Science");
            users.add(s1);
        }
        if (!hasUser("S2")) {
            Student s2 = new Student("S2", "Casey Lee", "casey@lms.com", "learn123");
            s2.setYearLevel("2nd Year");
            s2.setProgram("BS Information Technology");
            users.add(s2);
        }

        // ---- BS Computer Science curriculum ----
        Course cs101 = makeCourse("CS101", "Introduction to Computing", "1st Year | BS Computer Science", instructor, "BS Computer Science", "1st Year", true);
        Course cs102 = makeCourse("CS102", "Programming Fundamentals", "1st Year | BS Computer Science", instructor, "BS Computer Science", "1st Year", true);
        Course cs103 = makeCourse("CS103", "Discrete Mathematics", "1st Year | BS Computer Science", instructor, "BS Computer Science", "1st Year", false);
        Course cs201 = makeCourse("CS201", "Data Structures and Algorithms", "2nd Year | BS Computer Science", instructor, "BS Computer Science", "2nd Year", true);
        Course cs202 = makeCourse("CS202", "Object-Oriented Programming", "2nd Year | BS Computer Science", instructor, "BS Computer Science", "2nd Year", true);
        Course cs203 = makeCourse("CS203", "Computer Architecture", "2nd Year | BS Computer Science", instructor, "BS Computer Science", "2nd Year", false);
        Course cs301 = makeCourse("CS301", "Database Systems", "3rd Year | BS Computer Science", instructor, "BS Computer Science", "3rd Year", true);
        Course cs302 = makeCourse("CS302", "Operating Systems", "3rd Year | BS Computer Science", instructor, "BS Computer Science", "3rd Year", false);
        Course cs303 = makeCourse("CS303", "Software Engineering", "3rd Year | BS Computer Science", instructor, "BS Computer Science", "3rd Year", false);
        Course cs401 = makeCourse("CS401", "Capstone Project", "4th Year | BS Computer Science", instructor, "BS Computer Science", "4th Year", false);
        Course cs402 = makeCourse("CS402", "Elective: Machine Learning", "4th Year | BS Computer Science", instructor, "BS Computer Science", "4th Year", false);
        Course cs403 = makeCourse("CS403", "Elective: Cybersecurity", "4th Year | BS Computer Science", instructor, "BS Computer Science", "4th Year", false);
        Course cs104 = makeCourse("CS104", "Computer Fundamentals and Applications", "1st Year | BS Computer Science", instructor, "BS Computer Science", "1st Year", true);
        Course cs204 = makeCourse("CS204", "Web Development", "2nd Year | BS Computer Science", instructor, "BS Computer Science", "2nd Year", true);
        Course cs304 = makeCourse("CS304", "Artificial Intelligence", "3rd Year | BS Computer Science", instructor, "BS Computer Science", "3rd Year", false);
        Course cs305 = makeCourse("CS305", "Computer Networks", "3rd Year | BS Computer Science", instructor, "BS Computer Science", "3rd Year", false);
        Course cs306 = makeCourse("CS306", "Human-Computer Interaction", "3rd Year | BS Computer Science", instructor, "BS Computer Science", "3rd Year", false);
        Course cs404 = makeCourse("CS404", "Elective: Data Science", "4th Year | BS Computer Science", instructor, "BS Computer Science", "4th Year", false);
        Course cs405 = makeCourse("CS405", "Elective: Mobile App Development", "4th Year | BS Computer Science", instructor, "BS Computer Science", "4th Year", false);
        Course cs406 = makeCourse("CS406", "Elective: Cloud Computing", "4th Year | BS Computer Science", instructor, "BS Computer Science", "4th Year", false);

        // ---- BS Information Technology curriculum ----
        Course it101 = makeCourse("IT101", "Introduction to Computing", "1st Year | BS Information Technology", instructor, "BS Information Technology", "1st Year", true);
        Course it102 = makeCourse("IT102", "Web Development Fundamentals", "1st Year | BS Information Technology", instructor, "BS Information Technology", "1st Year", true);
        Course it103 = makeCourse("IT103", "IT Fundamentals and Ethics", "1st Year | BS Information Technology", instructor, "BS Information Technology", "1st Year", false);
        Course it201 = makeCourse("IT201", "Systems Analysis and Design", "2nd Year | BS Information Technology", instructor, "BS Information Technology", "2nd Year", true);
        Course it202 = makeCourse("IT202", "Network Fundamentals", "2nd Year | BS Information Technology", instructor, "BS Information Technology", "2nd Year", true);
        Course it203 = makeCourse("IT203", "Database Management", "2nd Year | BS Information Technology", instructor, "BS Information Technology", "2nd Year", false);
        Course it301 = makeCourse("IT301", "Systems Administration", "3rd Year | BS Information Technology", instructor, "BS Information Technology", "3rd Year", false);
        Course it302 = makeCourse("IT302", "IT Project Management", "3rd Year | BS Information Technology", instructor, "BS Information Technology", "3rd Year", false);
        Course it303 = makeCourse("IT303", "Elective: Cloud Computing", "3rd Year | BS Information Technology", instructor, "BS Information Technology", "3rd Year", false);
        Course it401 = makeCourse("IT401", "Practicum / OJT", "4th Year | BS Information Technology", instructor, "BS Information Technology", "4th Year", false);
        Course it402 = makeCourse("IT402", "Capstone Project", "4th Year | BS Information Technology", instructor, "BS Information Technology", "4th Year", false);
        Course it104 = makeCourse("IT104", "Programming Basics", "1st Year | BS Information Technology", instructor, "BS Information Technology", "1st Year", true);
        Course it204 = makeCourse("IT204", "Software Development", "2nd Year | BS Information Technology", instructor, "BS Information Technology", "2nd Year", true);
        Course it205 = makeCourse("IT205", "Web Application Development", "2nd Year | BS Information Technology", instructor, "BS Information Technology", "2nd Year", false);
        Course it304 = makeCourse("IT304", "Information Security", "3rd Year | BS Information Technology", instructor, "BS Information Technology", "3rd Year", false);
        Course it305 = makeCourse("IT305", "Enterprise Systems", "3rd Year | BS Information Technology", instructor, "BS Information Technology", "3rd Year", false);
        Course it306 = makeCourse("IT306", "Elective: AI Applications", "3rd Year | BS Information Technology", instructor, "BS Information Technology", "3rd Year", false);
        Course it403 = makeCourse("IT403", "Elective: DevOps", "4th Year | BS Information Technology", instructor, "BS Information Technology", "4th Year", false);
        Course it404 = makeCourse("IT404", "Elective: Data Analytics", "4th Year | BS Information Technology", instructor, "BS Information Technology", "4th Year", false);
        Course it405 = makeCourse("IT405", "Elective: IoT Fundamentals", "4th Year | BS Information Technology", instructor, "BS Information Technology", "4th Year", false);

        // ---- BS Electrical Engineering curriculum ----
        Course ee101 = makeCourse("EE101", "Engineering Mathematics 1", "1st Year | BS Electrical Engineering", instructor2, "BS Electrical Engineering", "1st Year", true);
        Course ee102 = makeCourse("EE102", "Basic Circuit Theory", "1st Year | BS Electrical Engineering", instructor2, "BS Electrical Engineering", "1st Year", true);
        Course ee201 = makeCourse("EE201", "Electromagnetics", "2nd Year | BS Electrical Engineering", instructor2, "BS Electrical Engineering", "2nd Year", false);
        Course ee202 = makeCourse("EE202", "Electronic Circuits", "2nd Year | BS Electrical Engineering", instructor2, "BS Electrical Engineering", "2nd Year", true);
        Course ee301 = makeCourse("EE301", "Power Systems", "3rd Year | BS Electrical Engineering", instructor2, "BS Electrical Engineering", "3rd Year", false);
        Course ee401 = makeCourse("EE401", "Elective: Renewable Energy", "4th Year | BS Electrical Engineering", instructor2, "BS Electrical Engineering", "4th Year", false);
        Course ee103 = makeCourse("EE103", "Engineering Mathematics 2", "1st Year | BS Electrical Engineering", instructor2, "BS Electrical Engineering", "1st Year", true);
        Course ee104 = makeCourse("EE104", "Physics for Engineers", "1st Year | BS Electrical Engineering", instructor2, "BS Electrical Engineering", "1st Year", true);
        Course ee105 = makeCourse("EE105", "Engineering Drawing", "1st Year | BS Electrical Engineering", instructor2, "BS Electrical Engineering", "1st Year", true);
        Course ee203 = makeCourse("EE203", "Digital Electronics", "2nd Year | BS Electrical Engineering", instructor2, "BS Electrical Engineering", "2nd Year", true);
        Course ee204 = makeCourse("EE204", "Signals and Systems", "2nd Year | BS Electrical Engineering", instructor2, "BS Electrical Engineering", "2nd Year", false);
        Course ee302 = makeCourse("EE302", "Control Systems", "3rd Year | BS Electrical Engineering", instructor2, "BS Electrical Engineering", "3rd Year", false);
        Course ee303 = makeCourse("EE303", "Microprocessors and Embedded Systems", "3rd Year | BS Electrical Engineering", instructor2, "BS Electrical Engineering", "3rd Year", false);
        Course ee304 = makeCourse("EE304", "Electrical Machines", "3rd Year | BS Electrical Engineering", instructor2, "BS Electrical Engineering", "3rd Year", false);
        Course ee402 = makeCourse("EE402", "Elective: Robotics", "4th Year | BS Electrical Engineering", instructor2, "BS Electrical Engineering", "4th Year", false);
        Course ee403 = makeCourse("EE403", "Elective: Power Electronics", "4th Year | BS Electrical Engineering", instructor2, "BS Electrical Engineering", "4th Year", false);
        Course ee404 = makeCourse("EE404", "Elective: VLSI Design", "4th Year | BS Electrical Engineering", instructor2, "BS Electrical Engineering", "4th Year", false);
        Course ee405 = makeCourse("EE405", "Elective: Smart Grid Technology", "4th Year | BS Electrical Engineering", instructor2, "BS Electrical Engineering", "4th Year", false);
        Course ee406 = makeCourse("EE406", "Elective: Automation and Instrumentation", "4th Year | BS Electrical Engineering", instructor2, "BS Electrical Engineering", "4th Year", false);
        Course ee407 = makeCourse("EE407", "Elective: Telecommunications", "4th Year | BS Electrical Engineering", instructor2, "BS Electrical Engineering", "4th Year", false);

        // ---- BS Mechanical Engineering curriculum ----
        Course me101 = makeCourse("ME101", "Engineering Drawing", "1st Year | BS Mechanical Engineering", instructor2, "BS Mechanical Engineering", "1st Year", true);
        Course me102 = makeCourse("ME102", "Statics and Dynamics", "1st Year | BS Mechanical Engineering", instructor2, "BS Mechanical Engineering", "1st Year", true);
        Course me201 = makeCourse("ME201", "Thermodynamics", "2nd Year | BS Mechanical Engineering", instructor2, "BS Mechanical Engineering", "2nd Year", false);
        Course me301 = makeCourse("ME301", "Fluid Mechanics", "3rd Year | BS Mechanical Engineering", instructor2, "BS Mechanical Engineering", "3rd Year", false);
        Course me401 = makeCourse("ME401", "Design Project", "4th Year | BS Mechanical Engineering", instructor2, "BS Mechanical Engineering", "4th Year", false);
        Course me103 = makeCourse("ME103", "Engineering Mathematics 2", "1st Year | BS Mechanical Engineering", instructor2, "BS Mechanical Engineering", "1st Year", true);
        Course me104 = makeCourse("ME104", "Physics for Engineers", "1st Year | BS Mechanical Engineering", instructor2, "BS Mechanical Engineering", "1st Year", true);
        Course me105 = makeCourse("ME105", "Technical Writing", "1st Year | BS Mechanical Engineering", instructor2, "BS Mechanical Engineering", "1st Year", false);
        Course me202 = makeCourse("ME202", "Material Science", "2nd Year | BS Mechanical Engineering", instructor2, "BS Mechanical Engineering", "2nd Year", true);
        Course me203 = makeCourse("ME203", "Machine Design", "2nd Year | BS Mechanical Engineering", instructor2, "BS Mechanical Engineering", "2nd Year", true);
        Course me204 = makeCourse("ME204", "Heat Transfer", "2nd Year | BS Mechanical Engineering", instructor2, "BS Mechanical Engineering", "2nd Year", false);
        Course me302 = makeCourse("ME302", "Control Engineering", "3rd Year | BS Mechanical Engineering", instructor2, "BS Mechanical Engineering", "3rd Year", false);
        Course me303 = makeCourse("ME303", "Manufacturing Processes", "3rd Year | BS Mechanical Engineering", instructor2, "BS Mechanical Engineering", "3rd Year", false);
        Course me304 = makeCourse("ME304", "Mechatronics", "3rd Year | BS Mechanical Engineering", instructor2, "BS Mechanical Engineering", "3rd Year", false);
        Course me402 = makeCourse("ME402", "Elective: Automotive Engineering", "4th Year | BS Mechanical Engineering", instructor2, "BS Mechanical Engineering", "4th Year", false);
        Course me403 = makeCourse("ME403", "Elective: Aerospace Engineering", "4th Year | BS Mechanical Engineering", instructor2, "BS Mechanical Engineering", "4th Year", false);
        Course me404 = makeCourse("ME404", "Elective: Renewable Energy Systems", "4th Year | BS Mechanical Engineering", instructor2, "BS Mechanical Engineering", "4th Year", false);
        Course me405 = makeCourse("ME405", "Elective: Industrial Automation", "4th Year | BS Mechanical Engineering", instructor2, "BS Mechanical Engineering", "4th Year", false);
        Course me406 = makeCourse("ME406", "Elective: Computational Methods", "4th Year | BS Mechanical Engineering", instructor2, "BS Mechanical Engineering", "4th Year", false);
        Course me407 = makeCourse("ME407", "Elective: HVAC Systems", "4th Year | BS Mechanical Engineering", instructor2, "BS Mechanical Engineering", "4th Year", false);

        // ---- BS Civil Engineering curriculum ----
        Course ce101 = makeCourse("CE101", "Engineering Mathematics 1", "1st Year | BS Civil Engineering", instructor2, "BS Civil Engineering", "1st Year", true);
        Course ce201 = makeCourse("CE201", "Strength of Materials", "2nd Year | BS Civil Engineering", instructor2, "BS Civil Engineering", "2nd Year", false);
        Course ce301 = makeCourse("CE301", "Structural Analysis", "3rd Year | BS Civil Engineering", instructor2, "BS Civil Engineering", "3rd Year", false);
        Course ce401 = makeCourse("CE401", "Construction Management", "4th Year | BS Civil Engineering", instructor2, "BS Civil Engineering", "4th Year", false);
        Course ce102 = makeCourse("CE102", "Engineering Mathematics 2", "1st Year | BS Civil Engineering", instructor2, "BS Civil Engineering", "1st Year", true);
        Course ce103 = makeCourse("CE103", "Physics for Engineers", "1st Year | BS Civil Engineering", instructor2, "BS Civil Engineering", "1st Year", true);
        Course ce104 = makeCourse("CE104", "Engineering Drawing", "1st Year | BS Civil Engineering", instructor2, "BS Civil Engineering", "1st Year", true);
        Course ce202 = makeCourse("CE202", "Surveying", "2nd Year | BS Civil Engineering", instructor2, "BS Civil Engineering", "2nd Year", true);
        Course ce203 = makeCourse("CE203", "Construction Materials", "2nd Year | BS Civil Engineering", instructor2, "BS Civil Engineering", "2nd Year", true);
        Course ce204 = makeCourse("CE204", "Geotechnical Engineering", "2nd Year | BS Civil Engineering", instructor2, "BS Civil Engineering", "2nd Year", false);
        Course ce302 = makeCourse("CE302", "Transportation Engineering", "3rd Year | BS Civil Engineering", instructor2, "BS Civil Engineering", "3rd Year", false);
        Course ce303 = makeCourse("CE303", "Hydraulics", "3rd Year | BS Civil Engineering", instructor2, "BS Civil Engineering", "3rd Year", false);
        Course ce304 = makeCourse("CE304", "Environmental Engineering", "3rd Year | BS Civil Engineering", instructor2, "BS Civil Engineering", "3rd Year", false);
        Course ce305 = makeCourse("CE305", "Earthquake Engineering", "3rd Year | BS Civil Engineering", instructor2, "BS Civil Engineering", "3rd Year", false);
        Course ce402 = makeCourse("CE402", "Elective: Sustainable Design", "4th Year | BS Civil Engineering", instructor2, "BS Civil Engineering", "4th Year", false);
        Course ce403 = makeCourse("CE403", "Elective: Project Management", "4th Year | BS Civil Engineering", instructor2, "BS Civil Engineering", "4th Year", false);
        Course ce404 = makeCourse("CE404", "Elective: Urban Planning", "4th Year | BS Civil Engineering", instructor2, "BS Civil Engineering", "4th Year", false);
        Course ce405 = makeCourse("CE405", "Elective: Water Resources", "4th Year | BS Civil Engineering", instructor2, "BS Civil Engineering", "4th Year", false);
        Course ce406 = makeCourse("CE406", "Elective: Coastal Engineering", "4th Year | BS Civil Engineering", instructor2, "BS Civil Engineering", "4th Year", false);
        Course ce407 = makeCourse("CE407", "Elective: Traffic Engineering", "4th Year | BS Civil Engineering", instructor2, "BS Civil Engineering", "4th Year", false);

        // ---- BS Accountancy curriculum ----
        Course ac101 = makeCourse("AC101", "Principles of Accounting 1", "1st Year | BS Accountancy", instructor, "BS Accountancy", "1st Year", true);
        Course ac102 = makeCourse("AC102", "Principles of Accounting 2", "1st Year | BS Accountancy", instructor, "BS Accountancy", "1st Year", true);
        Course ac201 = makeCourse("AC201", "Business Law", "2nd Year | BS Accountancy", instructor, "BS Accountancy", "2nd Year", false);
        Course ac301 = makeCourse("AC301", "Auditing", "3rd Year | BS Accountancy", instructor, "BS Accountancy", "3rd Year", false);
        Course ac401 = makeCourse("AC401", "Accounting Capstone", "4th Year | BS Accountancy", instructor, "BS Accountancy", "4th Year", false);

        // ---- BS Business Administration curriculum ----
        Course ba101 = makeCourse("BA101", "Principles of Management", "1st Year | BS Business Administration", instructor, "BS Business Administration", "1st Year", true);
        Course ba201 = makeCourse("BA201", "Marketing Principles", "2nd Year | BS Business Administration", instructor, "BS Business Administration", "2nd Year", false);
        Course ba301 = makeCourse("BA301", "Strategic Management", "3rd Year | BS Business Administration", instructor, "BS Business Administration", "3rd Year", false);
        Course ba401 = makeCourse("BA401", "Business Capstone", "4th Year | BS Business Administration", instructor, "BS Business Administration", "4th Year", false);

        // ---- BS Nursing curriculum ----
        Course nu101 = makeCourse("NU101", "Fundamentals of Nursing", "1st Year | BS Nursing", instructor, "BS Nursing", "1st Year", true);
        Course nu201 = makeCourse("NU201", "Medical-Surgical Nursing", "2nd Year | BS Nursing", instructor, "BS Nursing", "2nd Year", false);
        Course nu301 = makeCourse("NU301", "Community Health Nursing", "3rd Year | BS Nursing", instructor, "BS Nursing", "3rd Year", false);
        Course nu401 = makeCourse("NU401", "Nursing Practicum", "4th Year | BS Nursing", instructor, "BS Nursing", "4th Year", false);

        // ---- BS Education curriculum ----
        Course ed101 = makeCourse("ED101", "Foundations of Education", "1st Year | BS Education", instructor, "BS Education", "1st Year", true);
        Course ed201 = makeCourse("ED201", "Educational Psychology", "2nd Year | BS Education", instructor, "BS Education", "2nd Year", false);
        Course ed301 = makeCourse("ED301", "Teaching Methods", "3rd Year | BS Education", instructor, "BS Education", "3rd Year", false);
        Course ed401 = makeCourse("ED401", "Teaching Internship", "4th Year | BS Education", instructor, "BS Education", "4th Year", false);

        // ---- BS Architecture curriculum ----
        Course ar101 = makeCourse("AR101", "Architectural Drawing", "1st Year | BS Architecture", instructor2, "BS Architecture", "1st Year", true);
        Course ar201 = makeCourse("AR201", "Design Fundamentals", "2nd Year | BS Architecture", instructor2, "BS Architecture", "2nd Year", false);
        Course ar301 = makeCourse("AR301", "Building Technology", "3rd Year | BS Architecture", instructor2, "BS Architecture", "3rd Year", false);
        Course ar401 = makeCourse("AR401", "Thesis Design", "4th Year | BS Architecture", instructor2, "BS Architecture", "4th Year", false);

        // ---- BS Tourism Management curriculum ----
        Course tm101 = makeCourse("TM101", "Introduction to Tourism", "1st Year | BS Tourism Management", instructor, "BS Tourism Management", "1st Year", true);
        Course tm201 = makeCourse("TM201", "Hospitality Operations", "2nd Year | BS Tourism Management", instructor, "BS Tourism Management", "2nd Year", false);
        Course tm301 = makeCourse("TM301", "Tourism Planning", "3rd Year | BS Tourism Management", instructor, "BS Tourism Management", "3rd Year", false);
        Course tm401 = makeCourse("TM401", "Tourism Practicum", "4th Year | BS Tourism Management", instructor, "BS Tourism Management", "4th Year", false);

        // ---- BS Criminology curriculum ----
        Course cr101 = makeCourse("CR101", "Introduction to Criminology", "1st Year | BS Criminology", instructor, "BS Criminology", "1st Year", true);
        Course cr201 = makeCourse("CR201", "Criminal Law", "2nd Year | BS Criminology", instructor, "BS Criminology", "2nd Year", false);
        Course cr301 = makeCourse("CR301", "Forensic Science", "3rd Year | BS Criminology", instructor, "BS Criminology", "3rd Year", false);
        Course cr401 = makeCourse("CR401", "Criminology Practicum", "4th Year | BS Criminology", instructor, "BS Criminology", "4th Year", false);

        courses.addAll(
            cs101, cs102, cs103, cs104, cs201, cs202, cs203, cs204, cs301, cs302, cs303, cs304, cs305, cs306, cs401, cs402, cs403, cs404, cs405, cs406,
            it101, it102, it103, it104, it201, it202, it203, it204, it205, it301, it302, it303, it304, it305, it306, it401, it402, it403, it404, it405,
            ee101, ee102, ee103, ee104, ee105, ee201, ee202, ee203, ee204, ee301, ee302, ee303, ee304, ee401, ee402, ee403, ee404, ee405, ee406, ee407,
            me101, me102, me103, me104, me105, me201, me202, me203, me204, me301, me302, me303, me304, me401, me402, me403, me404, me405, me406, me407,
            ce101, ce102, ce103, ce104, ce201, ce202, ce203, ce204, ce301, ce302, ce303, ce304, ce305, ce401, ce402, ce403, ce404, ce405, ce406, ce407,
            ac101, ac102, ac201, ac301, ac401,
            ba101, ba201, ba301, ba401,
            nu101, nu201, nu301, nu401,
            ed101, ed201, ed301, ed401,
            ar101, ar201, ar301, ar401,
            tm101, tm201, tm301, tm401,
            cr101, cr201, cr301, cr401
        );

        instructor.getCoursesTaught().addAll(
            cs101, cs102, cs103, cs104, cs201, cs202, cs203, cs204, cs301, cs302, cs303, cs304, cs305, cs306, cs401, cs402, cs403, cs404, cs405, cs406,
            it101, it102, it103, it104, it201, it202, it203, it204, it205, it301, it302, it303, it304, it305, it306, it401, it402, it403, it404, it405,
            ac101, ac102, ac201, ac301, ac401,
            ba101, ba201, ba301, ba401,
            nu101, nu201, nu301, nu401,
            ed101, ed201, ed301, ed401,
            tm101, tm201, tm301, tm401,
            cr101, cr201, cr301, cr401
        );
        instructor2.getCoursesTaught().addAll(
            ee101, ee102, ee103, ee104, ee105, ee201, ee202, ee203, ee204, ee301, ee302, ee303, ee304, ee401, ee402, ee403, ee404, ee405, ee406, ee407,
            me101, me102, me103, me104, me105, me201, me202, me203, me204, me301, me302, me303, me304, me401, me402, me403, me404, me405, me406, me407,
            ce101, ce102, ce103, ce104, ce201, ce202, ce203, ce204, ce301, ce302, ce303, ce304, ce305, ce401, ce402, ce403, ce404, ce405, ce406, ce407,
            ar101, ar201, ar301, ar401
        );

        // Enroll demo students
        User s1User = findUser("S1");
        User s2User = findUser("S2");
        if (s1User instanceof Student student1) {
            student1.enroll(cs301);
            student1.enroll(cs302);
        }
        if (s2User instanceof Student student2) {
            student2.enroll(it201);
            student2.enroll(it202);
        }

        // Demo assignments
        Assignment a1 = new Assignment("AS1", "UML Diagram Set", LocalDate.now().plusDays(7), cs301);
        Assignment a2 = new Assignment("AS2", "Database Design Proposal", LocalDate.now().minusDays(2), cs301);
        cs301.getAssignments().addAll(a1, a2);
        cs301.getAnnouncements().add(new Announcement("AN1", "Welcome to Database Systems! Please review the syllabus."));
    }

    private Course makeCourse(String id, String title, String description, Instructor instructor,
                              String program, String yearLevel, boolean isPrerequisite) {
        Course c = new Course(id, title, description, instructor);
        c.setProgram(program);
        c.setYearLevel(yearLevel);
        c.setPrerequisite(isPrerequisite);
        return c;
    }

    private boolean hasUser(String userId) {
        return users.stream().anyMatch(u -> u.getUserId().equals(userId));
    }

    private User findUser(String userId) {
        return users.stream().filter(u -> u.getUserId().equals(userId)).findFirst().orElse(null);
    }

    // ---------------------------------------------------------------
    // Public API — used by UI layers
    // ---------------------------------------------------------------

    public ObservableList<User>   getUsers()   { return users; }
    public ObservableList<Course> getCourses() { return courses; }

    /**
     * Returns courses available to a student based on their program and year level.
     * A course is available if:
     *   - It matches the student's program
     *   - Its year level is <= the student's year level
     *   - The student is not already enrolled
     */
    public ObservableList<Course> getAvailableCourses(Student student) {
        ObservableList<Course> available = FXCollections.observableArrayList();
        if (student.getProgram() == null || student.getYearLevel() == null) return available;

        int studentYear = parseYearLevel(student.getYearLevel());

        for (Course c : courses) {
            if (c.getProgram() == null) continue;
            if (!c.getProgram().equals(student.getProgram())) continue;
            if (student.getEnrolledCourses().contains(c)) continue;

            int courseYear = parseYearLevel(c.getYearLevel());
            if (courseYear <= studentYear) {
                available.add(c);
            }
        }
        return available;
    }

    private int parseYearLevel(String yearLevel) {
        if (yearLevel == null) return 0;
        return switch (yearLevel) {
            case "1st Year" -> 1;
            case "2nd Year" -> 2;
            case "3rd Year" -> 3;
            case "4th Year" -> 4;
            case "5th Year" -> 5;
            default -> 0;
        };
    }

    /**
     * Authenticates a login attempt.
     * Only active (non-deleted) users can log in.
     */
    public User authenticate(String email, String password) {
        for (User u : users) {
            if (u.isActive()
                    && u.getEmail().equalsIgnoreCase(email)
                    && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    /** Finds any user by ID (including inactive), used for session restore. */
    public User findById(String userId) {
        for (User u : users) {
            if (u.getUserId().equals(userId)) return u;
        }
        return null;
    }

    /** Simple incrementing ID generator, e.g. generateId("C") -> "C104". */
    public String generateId(String prefix) {
        return prefix + (nextId++);
    }

    // ---------------------------------------------------------------
    // Mutating operations — always persist to DB if connected
    // ---------------------------------------------------------------

    public void addUser(User user) {
        users.add(user);
        DatabaseManager db = DatabaseManager.getInstance();
        if (db.isConnected()) db.saveUser(user);
    }

    /**
     * Soft-delete: marks the user inactive instead of removing the record.
     * The user disappears from login but the row stays in the DB with is_active = 0.
     */
    public void removeUser(User user) {
        user.setActive(false);
        // Refresh the observable list so the table updates
        int idx = users.indexOf(user);
        if (idx >= 0) {
            users.set(idx, user);
        }
        DatabaseManager db = DatabaseManager.getInstance();
        if (db.isConnected()) db.softDeleteUser(user);
    }

    public void addCourse(Course course) {
        courses.add(course);
        DatabaseManager db = DatabaseManager.getInstance();
        if (db.isConnected()) db.saveCourse(course);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        DatabaseManager db = DatabaseManager.getInstance();
        if (db.isConnected()) db.deleteCourse(course);
    }
}
