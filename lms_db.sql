-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 17, 2026 at 04:52 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `lms_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `announcements`
--

CREATE TABLE `announcements` (
  `announcement_id` varchar(20) NOT NULL,
  `message` text NOT NULL,
  `date_posted` date NOT NULL,
  `course_id` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `announcements`
--

INSERT INTO `announcements` (`announcement_id`, `message`, `date_posted`, `course_id`) VALUES
('AN1', 'Welcome to Database Systems! Please review the syllabus.', CURDATE(), 'CS301');

-- --------------------------------------------------------

--
-- Table structure for table `app_session`
--

CREATE TABLE `app_session` (
  `id` int(11) NOT NULL DEFAULT 1,
  `user_id` varchar(20) DEFAULT NULL,
  `last_tab` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `app_session`
--

INSERT INTO `app_session` (`id`, `user_id`, `last_tab`) VALUES
(1, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `assignments`
--

CREATE TABLE `assignments` (
  `assignment_id` varchar(20) NOT NULL,
  `title` varchar(200) NOT NULL,
  `due_date` date DEFAULT NULL,
  `course_id` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `assignments`
--

INSERT INTO `assignments` (`assignment_id`, `title`, `due_date`, `course_id`) VALUES
('AS1', 'UML Diagram Set', DATE_ADD(CURDATE(), INTERVAL 7 DAY), 'CS301'),
('AS2', 'Database Design Proposal', DATE_ADD(CURDATE(), INTERVAL -2 DAY), 'CS301');

-- --------------------------------------------------------

--
-- Table structure for table `courses`
--

CREATE TABLE `courses` (
  `course_id` varchar(20) NOT NULL,
  `title` varchar(200) NOT NULL,
  `description` text DEFAULT NULL,
  `instructor_id` varchar(20) DEFAULT NULL,
  `program` varchar(100) DEFAULT NULL,
  `year_level` varchar(50) DEFAULT NULL,
  `is_prerequisite` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `courses`
--

INSERT INTO `courses` (`course_id`, `title`, `description`, `instructor_id`, `program`, `year_level`, `is_prerequisite`) VALUES
('CS101', 'Introduction to Computing', '1st Year | BS Computer Science', 'I1', 'BS Computer Science', '1st Year', 1),
('CS102', 'Programming Fundamentals', '1st Year | BS Computer Science', 'I1', 'BS Computer Science', '1st Year', 1),
('CS103', 'Discrete Mathematics', '1st Year | BS Computer Science', 'I1', 'BS Computer Science', '1st Year', 0),
('CS104', 'Computer Fundamentals and Applications', '1st Year | BS Computer Science', 'I1', 'BS Computer Science', '1st Year', 1),
('CS201', 'Data Structures and Algorithms', '2nd Year | BS Computer Science', 'I1', 'BS Computer Science', '2nd Year', 1),
('CS202', 'Object-Oriented Programming', '2nd Year | BS Computer Science', 'I1', 'BS Computer Science', '2nd Year', 1),
('CS203', 'Computer Architecture', '2nd Year | BS Computer Science', 'I1', 'BS Computer Science', '2nd Year', 0),
('CS204', 'Web Development', '2nd Year | BS Computer Science', 'I1', 'BS Computer Science', '2nd Year', 1),
('CS301', 'Database Systems', '3rd Year | BS Computer Science', 'I1', 'BS Computer Science', '3rd Year', 1),
('CS302', 'Operating Systems', '3rd Year | BS Computer Science', 'I1', 'BS Computer Science', '3rd Year', 0),
('CS303', 'Software Engineering', '3rd Year | BS Computer Science', 'I1', 'BS Computer Science', '3rd Year', 0),
('CS304', 'Artificial Intelligence', '3rd Year | BS Computer Science', 'I1', 'BS Computer Science', '3rd Year', 0),
('CS305', 'Computer Networks', '3rd Year | BS Computer Science', 'I1', 'BS Computer Science', '3rd Year', 0),
('CS306', 'Human-Computer Interaction', '3rd Year | BS Computer Science', 'I1', 'BS Computer Science', '3rd Year', 0),
('CS401', 'Capstone Project', '4th Year | BS Computer Science', 'I1', 'BS Computer Science', '4th Year', 0),
('CS402', 'Elective: Machine Learning', '4th Year | BS Computer Science', 'I1', 'BS Computer Science', '4th Year', 0),
('CS403', 'Elective: Cybersecurity', '4th Year | BS Computer Science', 'I1', 'BS Computer Science', '4th Year', 0),
('CS404', 'Elective: Data Science', '4th Year | BS Computer Science', 'I1', 'BS Computer Science', '4th Year', 0),
('CS405', 'Elective: Mobile App Development', '4th Year | BS Computer Science', 'I1', 'BS Computer Science', '4th Year', 0),
('CS406', 'Elective: Cloud Computing', '4th Year | BS Computer Science', 'I1', 'BS Computer Science', '4th Year', 0),
('IT101', 'Introduction to Computing', '1st Year | BS Information Technology', 'I1', 'BS Information Technology', '1st Year', 1),
('IT102', 'Web Development Fundamentals', '1st Year | BS Information Technology', 'I1', 'BS Information Technology', '1st Year', 1),
('IT103', 'IT Fundamentals and Ethics', '1st Year | BS Information Technology', 'I1', 'BS Information Technology', '1st Year', 0),
('IT104', 'Programming Basics', '1st Year | BS Information Technology', 'I1', 'BS Information Technology', '1st Year', 1),
('IT201', 'Systems Analysis and Design', '2nd Year | BS Information Technology', 'I1', 'BS Information Technology', '2nd Year', 1),
('IT202', 'Network Fundamentals', '2nd Year | BS Information Technology', 'I1', 'BS Information Technology', '2nd Year', 1),
('IT203', 'Database Management', '2nd Year | BS Information Technology', 'I1', 'BS Information Technology', '2nd Year', 0),
('IT204', 'Software Development', '2nd Year | BS Information Technology', 'I1', 'BS Information Technology', '2nd Year', 1),
('IT205', 'Web Application Development', '2nd Year | BS Information Technology', 'I1', 'BS Information Technology', '2nd Year', 0),
('IT301', 'Systems Administration', '3rd Year | BS Information Technology', 'I1', 'BS Information Technology', '3rd Year', 0),
('IT302', 'IT Project Management', '3rd Year | BS Information Technology', 'I1', 'BS Information Technology', '3rd Year', 0),
('IT303', 'Elective: Cloud Computing', '3rd Year | BS Information Technology', 'I1', 'BS Information Technology', '3rd Year', 0),
('IT304', 'Information Security', '3rd Year | BS Information Technology', 'I1', 'BS Information Technology', '3rd Year', 0),
('IT305', 'Enterprise Systems', '3rd Year | BS Information Technology', 'I1', 'BS Information Technology', '3rd Year', 0),
('IT306', 'Elective: AI Applications', '3rd Year | BS Information Technology', 'I1', 'BS Information Technology', '3rd Year', 0),
('IT401', 'Practicum / OJT', '4th Year | BS Information Technology', 'I1', 'BS Information Technology', '4th Year', 0),
('IT402', 'Capstone Project', '4th Year | BS Information Technology', 'I1', 'BS Information Technology', '4th Year', 0),
('IT403', 'Elective: DevOps', '4th Year | BS Information Technology', 'I1', 'BS Information Technology', '4th Year', 0),
('IT404', 'Elective: Data Analytics', '4th Year | BS Information Technology', 'I1', 'BS Information Technology', '4th Year', 0),
('IT405', 'Elective: IoT Fundamentals', '4th Year | BS Information Technology', 'I1', 'BS Information Technology', '4th Year', 0),
('EE101', 'Engineering Mathematics 1', '1st Year | BS Electrical Engineering', 'I2', 'BS Electrical Engineering', '1st Year', 1),
('EE102', 'Basic Circuit Theory', '1st Year | BS Electrical Engineering', 'I2', 'BS Electrical Engineering', '1st Year', 1),
('EE103', 'Engineering Mathematics 2', '1st Year | BS Electrical Engineering', 'I2', 'BS Electrical Engineering', '1st Year', 1),
('EE104', 'Physics for Engineers', '1st Year | BS Electrical Engineering', 'I2', 'BS Electrical Engineering', '1st Year', 1),
('EE105', 'Engineering Drawing', '1st Year | BS Electrical Engineering', 'I2', 'BS Electrical Engineering', '1st Year', 1),
('EE201', 'Electromagnetics', '2nd Year | BS Electrical Engineering', 'I2', 'BS Electrical Engineering', '2nd Year', 0),
('EE202', 'Electronic Circuits', '2nd Year | BS Electrical Engineering', 'I2', 'BS Electrical Engineering', '2nd Year', 1),
('EE203', 'Digital Electronics', '2nd Year | BS Electrical Engineering', 'I2', 'BS Electrical Engineering', '2nd Year', 1),
('EE204', 'Signals and Systems', '2nd Year | BS Electrical Engineering', 'I2', 'BS Electrical Engineering', '2nd Year', 0),
('EE301', 'Power Systems', '3rd Year | BS Electrical Engineering', 'I2', 'BS Electrical Engineering', '3rd Year', 0),
('EE302', 'Control Systems', '3rd Year | BS Electrical Engineering', 'I2', 'BS Electrical Engineering', '3rd Year', 0),
('EE303', 'Microprocessors and Embedded Systems', '3rd Year | BS Electrical Engineering', 'I2', 'BS Electrical Engineering', '3rd Year', 0),
('EE304', 'Electrical Machines', '3rd Year | BS Electrical Engineering', 'I2', 'BS Electrical Engineering', '3rd Year', 0),
('EE401', 'Elective: Renewable Energy', '4th Year | BS Electrical Engineering', 'I2', 'BS Electrical Engineering', '4th Year', 0),
('EE402', 'Elective: Robotics', '4th Year | BS Electrical Engineering', 'I2', 'BS Electrical Engineering', '4th Year', 0),
('EE403', 'Elective: Power Electronics', '4th Year | BS Electrical Engineering', 'I2', 'BS Electrical Engineering', '4th Year', 0),
('EE404', 'Elective: VLSI Design', '4th Year | BS Electrical Engineering', 'I2', 'BS Electrical Engineering', '4th Year', 0),
('EE405', 'Elective: Smart Grid Technology', '4th Year | BS Electrical Engineering', 'I2', 'BS Electrical Engineering', '4th Year', 0),
('EE406', 'Elective: Automation and Instrumentation', '4th Year | BS Electrical Engineering', 'I2', 'BS Electrical Engineering', '4th Year', 0),
('EE407', 'Elective: Telecommunications', '4th Year | BS Electrical Engineering', 'I2', 'BS Electrical Engineering', '4th Year', 0),
('ME101', 'Engineering Drawing', '1st Year | BS Mechanical Engineering', 'I2', 'BS Mechanical Engineering', '1st Year', 1),
('ME102', 'Statics and Dynamics', '1st Year | BS Mechanical Engineering', 'I2', 'BS Mechanical Engineering', '1st Year', 1),
('ME103', 'Engineering Mathematics 2', '1st Year | BS Mechanical Engineering', 'I2', 'BS Mechanical Engineering', '1st Year', 1),
('ME104', 'Physics for Engineers', '1st Year | BS Mechanical Engineering', 'I2', 'BS Mechanical Engineering', '1st Year', 1),
('ME105', 'Technical Writing', '1st Year | BS Mechanical Engineering', 'I2', 'BS Mechanical Engineering', '1st Year', 0),
('ME201', 'Thermodynamics', '2nd Year | BS Mechanical Engineering', 'I2', 'BS Mechanical Engineering', '2nd Year', 0),
('ME202', 'Material Science', '2nd Year | BS Mechanical Engineering', 'I2', 'BS Mechanical Engineering', '2nd Year', 1),
('ME203', 'Machine Design', '2nd Year | BS Mechanical Engineering', 'I2', 'BS Mechanical Engineering', '2nd Year', 1),
('ME204', 'Heat Transfer', '2nd Year | BS Mechanical Engineering', 'I2', 'BS Mechanical Engineering', '2nd Year', 0),
('ME301', 'Fluid Mechanics', '3rd Year | BS Mechanical Engineering', 'I2', 'BS Mechanical Engineering', '3rd Year', 0),
('ME302', 'Control Engineering', '3rd Year | BS Mechanical Engineering', 'I2', 'BS Mechanical Engineering', '3rd Year', 0),
('ME303', 'Manufacturing Processes', '3rd Year | BS Mechanical Engineering', 'I2', 'BS Mechanical Engineering', '3rd Year', 0),
('ME304', 'Mechatronics', '3rd Year | BS Mechanical Engineering', 'I2', 'BS Mechanical Engineering', '3rd Year', 0),
('ME401', 'Design Project', '4th Year | BS Mechanical Engineering', 'I2', 'BS Mechanical Engineering', '4th Year', 0),
('ME402', 'Elective: Automotive Engineering', '4th Year | BS Mechanical Engineering', 'I2', 'BS Mechanical Engineering', '4th Year', 0),
('ME403', 'Elective: Aerospace Engineering', '4th Year | BS Mechanical Engineering', 'I2', 'BS Mechanical Engineering', '4th Year', 0),
('ME404', 'Elective: Renewable Energy Systems', '4th Year | BS Mechanical Engineering', 'I2', 'BS Mechanical Engineering', '4th Year', 0),
('ME405', 'Elective: Industrial Automation', '4th Year | BS Mechanical Engineering', 'I2', 'BS Mechanical Engineering', '4th Year', 0),
('ME406', 'Elective: Computational Methods', '4th Year | BS Mechanical Engineering', 'I2', 'BS Mechanical Engineering', '4th Year', 0),
('ME407', 'Elective: HVAC Systems', '4th Year | BS Mechanical Engineering', 'I2', 'BS Mechanical Engineering', '4th Year', 0),
('CE101', 'Engineering Mathematics 1', '1st Year | BS Civil Engineering', 'I2', 'BS Civil Engineering', '1st Year', 1),
('CE102', 'Engineering Mathematics 2', '1st Year | BS Civil Engineering', 'I2', 'BS Civil Engineering', '1st Year', 1),
('CE103', 'Physics for Engineers', '1st Year | BS Civil Engineering', 'I2', 'BS Civil Engineering', '1st Year', 1),
('CE104', 'Engineering Drawing', '1st Year | BS Civil Engineering', 'I2', 'BS Civil Engineering', '1st Year', 1),
('CE201', 'Strength of Materials', '2nd Year | BS Civil Engineering', 'I2', 'BS Civil Engineering', '2nd Year', 0),
('CE202', 'Surveying', '2nd Year | BS Civil Engineering', 'I2', 'BS Civil Engineering', '2nd Year', 1),
('CE203', 'Construction Materials', '2nd Year | BS Civil Engineering', 'I2', 'BS Civil Engineering', '2nd Year', 1),
('CE204', 'Geotechnical Engineering', '2nd Year | BS Civil Engineering', 'I2', 'BS Civil Engineering', '2nd Year', 0),
('CE301', 'Structural Analysis', '3rd Year | BS Civil Engineering', 'I2', 'BS Civil Engineering', '3rd Year', 0),
('CE302', 'Transportation Engineering', '3rd Year | BS Civil Engineering', 'I2', 'BS Civil Engineering', '3rd Year', 0),
('CE303', 'Hydraulics', '3rd Year | BS Civil Engineering', 'I2', 'BS Civil Engineering', '3rd Year', 0),
('CE304', 'Environmental Engineering', '3rd Year | BS Civil Engineering', 'I2', 'BS Civil Engineering', '3rd Year', 0),
('CE305', 'Earthquake Engineering', '3rd Year | BS Civil Engineering', 'I2', 'BS Civil Engineering', '3rd Year', 0),
('CE401', 'Construction Management', '4th Year | BS Civil Engineering', 'I2', 'BS Civil Engineering', '4th Year', 0),
('CE402', 'Elective: Sustainable Design', '4th Year | BS Civil Engineering', 'I2', 'BS Civil Engineering', '4th Year', 0),
('CE403', 'Elective: Project Management', '4th Year | BS Civil Engineering', 'I2', 'BS Civil Engineering', '4th Year', 0),
('CE404', 'Elective: Urban Planning', '4th Year | BS Civil Engineering', 'I2', 'BS Civil Engineering', '4th Year', 0),
('CE405', 'Elective: Water Resources', '4th Year | BS Civil Engineering', 'I2', 'BS Civil Engineering', '4th Year', 0),
('CE406', 'Elective: Coastal Engineering', '4th Year | BS Civil Engineering', 'I2', 'BS Civil Engineering', '4th Year', 0),
('CE407', 'Elective: Traffic Engineering', '4th Year | BS Civil Engineering', 'I2', 'BS Civil Engineering', '4th Year', 0),
('AC101', 'Principles of Accounting 1', '1st Year | BS Accountancy', 'I1', 'BS Accountancy', '1st Year', 1),
('AC102', 'Principles of Accounting 2', '1st Year | BS Accountancy', 'I1', 'BS Accountancy', '1st Year', 1),
('AC201', 'Business Law', '2nd Year | BS Accountancy', 'I1', 'BS Accountancy', '2nd Year', 0),
('AC301', 'Auditing', '3rd Year | BS Accountancy', 'I1', 'BS Accountancy', '3rd Year', 0),
('AC401', 'Accounting Capstone', '4th Year | BS Accountancy', 'I1', 'BS Accountancy', '4th Year', 0),
('BA101', 'Principles of Management', '1st Year | BS Business Administration', 'I1', 'BS Business Administration', '1st Year', 1),
('BA201', 'Marketing Principles', '2nd Year | BS Business Administration', 'I1', 'BS Business Administration', '2nd Year', 0),
('BA301', 'Strategic Management', '3rd Year | BS Business Administration', 'I1', 'BS Business Administration', '3rd Year', 0),
('BA401', 'Business Capstone', '4th Year | BS Business Administration', 'I1', 'BS Business Administration', '4th Year', 0),
('NU101', 'Fundamentals of Nursing', '1st Year | BS Nursing', 'I1', 'BS Nursing', '1st Year', 1),
('NU201', 'Medical-Surgical Nursing', '2nd Year | BS Nursing', 'I1', 'BS Nursing', '2nd Year', 0),
('NU301', 'Community Health Nursing', '3rd Year | BS Nursing', 'I1', 'BS Nursing', '3rd Year', 0),
('NU401', 'Nursing Practicum', '4th Year | BS Nursing', 'I1', 'BS Nursing', '4th Year', 0),
('ED101', 'Foundations of Education', '1st Year | BS Education', 'I1', 'BS Education', '1st Year', 1),
('ED201', 'Educational Psychology', '2nd Year | BS Education', 'I1', 'BS Education', '2nd Year', 0),
('ED301', 'Teaching Methods', '3rd Year | BS Education', 'I1', 'BS Education', '3rd Year', 0),
('ED401', 'Teaching Internship', '4th Year | BS Education', 'I1', 'BS Education', '4th Year', 0),
('AR101', 'Architectural Drawing', '1st Year | BS Architecture', 'I2', 'BS Architecture', '1st Year', 1),
('AR201', 'Design Fundamentals', '2nd Year | BS Architecture', 'I2', 'BS Architecture', '2nd Year', 0),
('AR301', 'Building Technology', '3rd Year | BS Architecture', 'I2', 'BS Architecture', '3rd Year', 0),
('AR401', 'Thesis Design', '4th Year | BS Architecture', 'I2', 'BS Architecture', '4th Year', 0),
('TM101', 'Introduction to Tourism', '1st Year | BS Tourism Management', 'I1', 'BS Tourism Management', '1st Year', 1),
('TM201', 'Hospitality Operations', '2nd Year | BS Tourism Management', 'I1', 'BS Tourism Management', '2nd Year', 0),
('TM301', 'Tourism Planning', '3rd Year | BS Tourism Management', 'I1', 'BS Tourism Management', '3rd Year', 0),
('TM401', 'Tourism Practicum', '4th Year | BS Tourism Management', 'I1', 'BS Tourism Management', '4th Year', 0),
('CR101', 'Introduction to Criminology', '1st Year | BS Criminology', 'I1', 'BS Criminology', '1st Year', 1),
('CR201', 'Criminal Law', '2nd Year | BS Criminology', 'I1', 'BS Criminology', '2nd Year', 0),
('CR301', 'Forensic Science', '3rd Year | BS Criminology', 'I1', 'BS Criminology', '3rd Year', 0),
('CR401', 'Criminology Practicum', '4th Year | BS Criminology', 'I1', 'BS Criminology', '4th Year', 0);

-- --------------------------------------------------------

--
-- Table structure for table `enrollments`
--

CREATE TABLE `enrollments` (
  `student_id` varchar(20) NOT NULL,
  `course_id` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `grades`
--

CREATE TABLE `grades` (
  `grade_id` varchar(20) NOT NULL,
  `submission_id` varchar(20) NOT NULL,
  `score` double NOT NULL,
  `feedback` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `submissions`
--

CREATE TABLE `submissions` (
  `submission_id` varchar(20) NOT NULL,
  `student_id` varchar(20) NOT NULL,
  `assignment_id` varchar(20) NOT NULL,
  `file_name` varchar(200) DEFAULT NULL,
  `submitted_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` varchar(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` varchar(20) NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  `year_level` varchar(50) DEFAULT NULL,
  `program` varchar(100) DEFAULT NULL,
  `department` varchar(100) DEFAULT NULL,
  `employee_id` varchar(30) DEFAULT NULL,
  `designation` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `name`, `email`, `password`, `role`, `is_active`, `year_level`, `program`, `department`, `employee_id`, `designation`) VALUES
('A1', 'Alex Admin', 'admin@lms.com', 'admin123', 'Administrator', 1, NULL, NULL, NULL, NULL, NULL),
('I1', 'Dr. Jamie Cruz', 'jamie@lms.com', 'teach123', 'Instructor', 1, NULL, NULL, 'College of Computer Studies', NULL, 'Professor III'),
('I2', 'Prof. Maria Santos', 'maria@lms.com', 'teach123', 'Instructor', 1, NULL, NULL, 'College of Engineering', NULL, 'Professor II'),
('S1', 'Sam Rivera', 'sam@lms.com', 'learn123', 'Student', 1, '3rd Year', 'BS Computer Science', NULL, NULL, NULL),
('S2', 'Casey Lee', 'casey@lms.com', 'learn123', 'Student', 1, '2nd Year', 'BS Information Technology', NULL, NULL, NULL),
('S3', 'Brent Brynner A. Coraler', 'brentcoraler@gmail.com', 'brent123', 'Student', 1, NULL, NULL, NULL, NULL, NULL),
('S4', 'baba', 'baba@lms.com', 'baba123', 'Student', 0, NULL, NULL, NULL, NULL, NULL),
('S5', 'Andre Coraler', 'andre@gmail.com', 'andre123', 'Student', 1, '3rd Year', 'BS Information Technology', NULL, NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `announcements`
--
ALTER TABLE `announcements`
  ADD PRIMARY KEY (`announcement_id`),
  ADD KEY `course_id` (`course_id`);

--
-- Indexes for table `app_session`
--
ALTER TABLE `app_session`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `assignments`
--
ALTER TABLE `assignments`
  ADD PRIMARY KEY (`assignment_id`),
  ADD KEY `course_id` (`course_id`);

--
-- Indexes for table `courses`
--
ALTER TABLE `courses`
  ADD PRIMARY KEY (`course_id`),
  ADD KEY `instructor_id` (`instructor_id`);

--
-- Indexes for table `enrollments`
--
ALTER TABLE `enrollments`
  ADD PRIMARY KEY (`student_id`,`course_id`),
  ADD KEY `course_id` (`course_id`);

--
-- Indexes for table `grades`
--
ALTER TABLE `grades`
  ADD PRIMARY KEY (`grade_id`),
  ADD UNIQUE KEY `submission_id` (`submission_id`);

--
-- Indexes for table `submissions`
--
ALTER TABLE `submissions`
  ADD PRIMARY KEY (`submission_id`),
  ADD KEY `student_id` (`student_id`),
  ADD KEY `assignment_id` (`assignment_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `announcements`
--
ALTER TABLE `announcements`
  ADD CONSTRAINT `announcements_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`);

--
-- Constraints for table `assignments`
--
ALTER TABLE `assignments`
  ADD CONSTRAINT `assignments_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`);

--
-- Constraints for table `courses`
--
ALTER TABLE `courses`
  ADD CONSTRAINT `courses_ibfk_1` FOREIGN KEY (`instructor_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `enrollments`
--
ALTER TABLE `enrollments`
  ADD CONSTRAINT `enrollments_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `enrollments_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`);

--
-- Constraints for table `grades`
--
ALTER TABLE `grades`
  ADD CONSTRAINT `grades_ibfk_1` FOREIGN KEY (`submission_id`) REFERENCES `submissions` (`submission_id`);

--
-- Constraints for table `submissions`
--
ALTER TABLE `submissions`
  ADD CONSTRAINT `submissions_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `submissions_ibfk_2` FOREIGN KEY (`assignment_id`) REFERENCES `assignments` (`assignment_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
