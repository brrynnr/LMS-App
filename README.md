# Learning Management System (LMS)

## Overview

The Learning Management System (LMS) is a desktop application developed in JavaFX that allows Students, Instructors, and Administrators to manage learning activities through a simple graphical user interface. The system supports user authentication, course management, student enrollment, grading, and session management.

---

## Major Features

### Authentication
- User Login
- User Registration
- Role-based access (Student, Instructor, Administrator)

### Student Features
- View available courses
- Enroll in courses
- View enrolled courses
- View grades

### Instructor Features
- View assigned courses
- Manage enrolled students
- Assign grades

### Administrator Features
- Add users
- Deactivate users
- Add courses
- Remove courses

---

# Java Serialization

This project implements Java Serialization for user session management.

After a successful login, the application creates a serialized file named:

```
session.dat
```

The file stores the logged-in user's session information (User ID and Role).

When the application starts, the serialized file is checked automatically.

If a valid session exists, the application restores the user's session and opens the appropriate dashboard without requiring another login.

When the user logs out, the session file is automatically deleted.

---

# SOLID Principles Applied

## 1. Single Responsibility Principle (SRP)

### Class

```
SessionManager
```

### Responsibility

The SessionManager class is responsible only for managing serialized user sessions.

Its responsibilities include:

- Saving session data
- Loading session data
- Checking if a session exists
- Deleting session data

It does not perform authentication, user interface management, or database operations.

### Benefit

- Easier maintenance
- Cleaner code
- Better separation of concerns

---

## 2. Interface Segregation Principle (ISP)

### Interface

```
SessionService
```

### Implementation

```
SessionManager implements SessionService
```

The application depends on the SessionService interface instead of directly depending on SessionManager.

This reduces coupling and makes it easier to replace or extend the session management implementation in the future.

### Benefit

- Loose coupling
- Better flexibility
- Easier testing
- Easier future maintenance

---

# Technologies Used

- Java 26
- JavaFX 26
- IntelliJ IDEA
- MySQL
- Java Serialization
- Object-Oriented Programming (OOP)

---

# Authors

Brent Coraler
