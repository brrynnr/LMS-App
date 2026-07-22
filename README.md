# Learning Management System (LMS)

![Java](https://img.shields.io/badge/Java-26-red)
![JavaFX](https://img.shields.io/badge/JavaFX-26-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)
![Status](https://img.shields.io/badge/Status-Completed-success)

A desktop-based **Learning Management System (LMS)** developed using **JavaFX**, **MySQL**, and **Java Serialization**. The application provides Students, Instructors, and Administrators with an intuitive platform for managing courses, enrollments, assignments, announcements, grading, and user accounts through a modern graphical interface.

---

# Table of Contents

- Overview
- Features
- System Architecture
- UML Diagrams
- Database Design
- Technologies Used
- Installation Guide
- Project Structure
- Java Serialization
- SOLID Principles Applied
- Future Improvements
- Authors

---

# Overview

The Learning Management System (LMS) was developed as a capstone project for **Object-Oriented Programming 2**. The system demonstrates the application of Object-Oriented Programming principles, JavaFX GUI development, database management using MySQL, and persistent session management through Java Serialization.

The system supports three user roles:

- Student
- Instructor
- Administrator

Each role has its own dashboard with specific permissions and functionality.

---

# Features

## Authentication

- User Login
- User Registration
- Role-based Authentication
- Automatic Session Restoration
- Secure Logout

---

## Student Features

Students can:

- View Available Courses
- Enroll in Courses
- View Enrolled Courses
- Submit Assignments
- View Grades
- View Course Announcements

---

## Instructor Features

Instructors can:

- View Assigned Courses
- Post Course Announcements
- Grade Student Submissions
- View Student Enrollments
- Manage Course Activities

---

## Administrator Features

Administrators can:

- Register Users
- Activate or Deactivate Users
- Add Courses
- Remove Courses
- Manage User Accounts

---

# System Architecture

The application follows a layered architecture that separates presentation, business logic, and database access.

```
JavaFX User Interface
        │
        ▼
Business Logic Layer
        │
        ▼
Database Facade
        │
        ▼
DatabaseManager (JDBC)
        │
        ▼
MySQL Database
```

This architecture promotes modularity, maintainability, and scalability.

---

# UML Diagrams

The following UML diagrams were created during the system design phase.

## Use Case Diagram

<img width="736" height="652" alt="useCaseDiagram drawio" src="https://github.com/user-attachments/assets/bdb77f91-7a0b-4696-a8ed-31acc919f8a4" />


---

## Activity Diagram

<img width="762" height="902" alt="ActivityDiagram drawio" src="https://github.com/user-attachments/assets/5ccf5891-5ba1-4457-b313-7ab519f14832" />


---

## Sequence Diagram

<img width="1052" height="663" alt="SequenceDiagram drawio" src="https://github.com/user-attachments/assets/a8dcb66a-fd15-4b8d-aedd-e9a3fca1fb41" />


---

## Class Diagram

<img width="1952" height="1237" alt="classDiagram drawio" src="https://github.com/user-attachments/assets/ff0b25d5-7d8d-43d0-8eff-4e34192b310c" />


---

# Database Design

The application uses **MySQL** for persistent data storage.

### Database Name

```
lms_db
```

### Main Tables

| Table | Description |
|--------|-------------|
| users | Stores user accounts |
| courses | Stores all available courses |
| enrollments | Student course enrollments |
| assignments | Course assignments |
| submissions | Student assignment submissions |
| announcements | Course announcements |
| grades | Student grades |
| app_session | Optional application session information |

The database currently includes:

- 129 Courses
- 12 Academic Programs
- Administrator Account
- Instructor Accounts
- Student Accounts
- Demo Assignments
- Demo Announcements

### Supported Academic Programs

- Computer Science
- Information Technology
- Civil Engineering
- Mechanical Engineering
- Electrical Engineering
- Accountancy
- Business Administration
- Nursing
- Education
- Architecture
- Tourism
- Criminology

---

# Technologies Used

| Technology | Purpose |
|------------|----------|
| Java 26 | Programming Language |
| JavaFX 26 | Desktop User Interface |
| MySQL 8 | Database |
| JDBC | Database Connectivity |
| Java Serialization | Session Management |
| IntelliJ IDEA | IDE |
| Git | Version Control |
| GitHub | Repository Hosting |

---

# Installation Guide

## 1. Clone the Repository

```bash
git clone https://github.com/brrynnr/LMS-App.git
```

---

## 2. Open the Project

Open the project using IntelliJ IDEA.

---

## 3. Configure JavaFX

Download JavaFX SDK 26 and add the following VM options:

```text
--module-path "PATH_TO_JAVAFX/lib"
--add-modules javafx.controls,javafx.fxml
```

---

## 4. Configure MySQL

1. Install XAMPP or MySQL Server.
2. Start Apache and MySQL.
3. Open phpMyAdmin.
4. Create a database named:

```
lms_db
```

5. Import the provided:

```
lms_db.sql
```

---

## 5. Configure Database Connection

Example:

```java
private static final String URL =
    "jdbc:mysql://localhost:3306/lms_db";

private static final String USER = "root";

private static final String PASSWORD = "";
```

Modify the credentials if your MySQL installation uses a password.

---

## 6. Run the Application

Run:

```
Main.java
```

---

# Project Structure

```
LMS-App
│
├── src
│   ├── com.lms.app
│   ├── model
│   ├── view
│   ├── controller
│   ├── database
│   ├── service
│   ├── util
│   └── Main.java
│
├── resources
│
├── lms_db.sql
│
└── README.md
```

---

# Java Serialization

The project implements Java Serialization for persistent user sessions.

After a successful login, the application creates a serialized file named:

```
session.dat
```

The serialized file stores:

- User ID
- User Role

When the application starts, it automatically checks for an existing session.

If a valid session exists, the system restores the user's session and opens the appropriate dashboard without requiring another login.

When the user logs out, the session file is automatically deleted.

---

# SOLID Principles Applied

## Single Responsibility Principle (SRP)

### SessionManager

The `SessionManager` class is responsible only for managing serialized user sessions.

Responsibilities include:

- Saving session data
- Loading session data
- Deleting session data
- Checking whether a session exists

The class does not perform authentication, database operations, or user interface management.

### Benefits

- Easier maintenance
- Better separation of concerns
- Cleaner code structure

---

## Interface Segregation Principle (ISP)

### SessionService

```
SessionManager implements SessionService
```

The application depends on the `SessionService` interface instead of the concrete implementation.

This makes the system easier to extend, test, and maintain.

### Benefits

- Loose Coupling
- Better Flexibility
- Easier Unit Testing
- Improved Maintainability

---

# Future Improvements

Potential future enhancements include:

- Password Encryption
- Email Verification
- Quiz and Examination Module
- Attendance Tracking
- File Upload Support
- Student Progress Analytics
- Dark Mode
- Cloud Database Integration
- Email Notifications

---

# Authors

**Brent Coraler**

Bachelor of Science in Computer Science

Capstone Project

Object-Oriented Programming 2
