package com.lms.app.model;

import java.time.LocalDate;

public class Announcement {

    private final String announcementId;
    private final String message;
    private final LocalDate datePosted;

    public Announcement(String announcementId, String message) {
        this.announcementId = announcementId;
        this.message = message;
        this.datePosted = LocalDate.now();
    }

    public String getAnnouncementId() { return announcementId; }
    public String getMessage() { return message; }
    public LocalDate getDatePosted() { return datePosted; }
}
